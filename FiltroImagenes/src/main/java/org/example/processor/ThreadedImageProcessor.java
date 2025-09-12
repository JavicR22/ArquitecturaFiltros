package org.example.processor;

import org.example.filters.ImageFilter;
import org.example.io.ImageData;
import org.example.io.InputSource;
import org.example.io.OutputSink;
import org.example.io.EnhancedImageData;
import org.example.io.EnhancedFileOutputSink;
import org.example.validation.ValidationUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Procesador de imágenes con soporte para múltiples hilos
 * y manejo thread-safe de operaciones de archivo
 */
public class ThreadedImageProcessor extends ImageProcessor {
    private final int threadCount;
    private final AtomicInteger processedCount = new AtomicInteger(0);

    public ThreadedImageProcessor(InputSource inputSource, OutputSink outputSink, List<ImageFilter> filters, int threadCount) {
        super(inputSource, outputSink, filters);

        if (threadCount <= 0) {
            throw new IllegalArgumentException("Error: el número de hilos debe ser mayor a 0");
        }

        this.threadCount = threadCount;
    }


    public String procesarImagenes() throws IOException {
        if (!inputSource.hasImages()) {
            throw new IOException("No hay imágenes disponibles para procesar");
        }

        List<ImageData> images = inputSource.getImages();

        if (images.isEmpty()) {
            throw new IOException("Error: no se encontraron imágenes válidas para procesar");
        }

        System.err.println("Procesando " + images.size() + " imagen(es) con " + threadCount + " hilos...");

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        try {
            for (ImageData imageData : images) {
                executor.submit(() -> procesarImagenThreadSafe(imageData));
            }

            executor.shutdown();
            if (!executor.awaitTermination(30, TimeUnit.MINUTES)) {
                System.err.println("⚠️  Timeout: algunos hilos no terminaron en el tiempo esperado");
                executor.shutdownNow();
            }

        } catch (InterruptedException e) {
            System.err.println("❌ Error: procesamiento interrumpido");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            throw new IOException("Procesamiento interrumpido", e);
        }

        System.err.println("Procesamiento completado. Total procesadas: " + processedCount.get());

        if (outputSink instanceof EnhancedFileOutputSink) {
            return ((EnhancedFileOutputSink) outputSink).getOutputRootPath();
        }

        return null;
    }

    private void procesarImagenThreadSafe(ImageData imageData) {
        try {
            if (imageData == null) {
                System.err.println("❌ Error: datos de imagen nulos");
                return;
            }

            ValidationUtils.validateImage(imageData.getImage(), imageData.getName());

            String initializationKey = imageData.getName();
            if (imageData instanceof EnhancedImageData) {
                EnhancedImageData enhanced = (EnhancedImageData) imageData;
                initializationKey = enhanced.getName() + "|ENHANCED|" + enhanced.getRelativePath();
            }

            synchronized (outputSink) {
                // Initialize for this image
                outputSink.initializeForImage(initializationKey);

                // Save original image
                outputSink.saveImage(imageData.getImage(), imageData.getName(), null, imageData.getExtension());
                System.err.println("  ✅ Imagen original procesada: " + imageData.getName());

                // Apply all filters for this image
                for (ImageFilter filter : filters) {
                    try {
                        if (filter == null) {
                            System.err.println("  ❌ Error: filtro nulo encontrado");
                            continue;
                        }

                        BufferedImage resultado = filter.aplicar(imageData.getImage());

                        if (resultado == null) {
                            System.err.println("  ❌ Error: el filtro " + filter.getNombre() + " devolvió una imagen nula");
                            continue;
                        }

                        ValidationUtils.validateImage(resultado, imageData.getName() + "_" + filter.getNombre());

                        outputSink.saveImage(resultado, imageData.getName(), filter.getNombre(), imageData.getExtension());
                        System.err.println("  ✅ Filtro aplicado: " + filter.getNombre() + " a " + imageData.getName());
                    } catch (Exception e) {
                        System.err.println("  ❌ Error aplicando filtro: " + filter.getNombre() + " - " + e.getMessage());
                    }
                }

                // Finalize this image
                outputSink.finalizeImage();
            }

            processedCount.incrementAndGet();

        } catch (Exception e) {
            System.err.println("❌ Error procesando imagen: " + imageData.getName() + " - " + e.getMessage());
        }
    }
}

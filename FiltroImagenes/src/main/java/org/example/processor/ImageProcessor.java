package org.example.processor;

import org.example.filters.ImageFilter;
import org.example.io.ImageData;
import org.example.io.InputSource;
import org.example.io.OutputSink;
import org.example.validation.ValidationUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class ImageProcessor {
    protected final InputSource inputSource;
    protected final OutputSink outputSink;
    protected final List<ImageFilter> filters;

    public ImageProcessor(InputSource inputSource, OutputSink outputSink, List<ImageFilter> filters) {
        if (inputSource == null) {
            throw new IllegalArgumentException("Error: InputSource no puede ser nulo");
        }
        if (outputSink == null) {
            throw new IllegalArgumentException("Error: OutputSink no puede ser nulo");
        }
        if (filters == null || filters.isEmpty()) {
            throw new IllegalArgumentException("Error: la lista de filtros no puede ser nula o vacía");
        }

        this.inputSource = inputSource;
        this.outputSink = outputSink;
        this.filters = filters;
    }

    public String procesar() throws IOException {
        if (!inputSource.hasImages()) {
            throw new IOException("No hay imágenes disponibles para procesar");
        }

        List<ImageData> images = inputSource.getImages();

        if (images.isEmpty()) {
            throw new IOException("Error: no se encontraron imágenes válidas para procesar");
        }

        System.err.println("Procesando " + images.size() + " imagen(es)...");

        for (ImageData imageData : images) {
            procesarImagen(imageData);
        }

        System.err.println("Procesamiento completado.");

        return null;
    }

    private void procesarImagen(ImageData imageData) {
        try {
            if (imageData == null) {
                System.err.println("❌ Error: datos de imagen nulos");
                return;
            }

            ValidationUtils.validateImage(imageData.getImage(), imageData.getName());

            outputSink.initializeForImage(imageData.getName());

            outputSink.saveImage(imageData.getImage(), imageData.getName(), null, imageData.getExtension());
            System.err.println("  ✅ Imagen original procesada: " + imageData.getName());

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
                    System.err.println("  ✅ Filtro aplicado: " + filter.getNombre());
                } catch (Exception e) {
                    System.err.println("  ❌ Error aplicando filtro: " + filter.getNombre() + " - " + e.getMessage());
                }
            }

            outputSink.finalizeImage();

        } catch (Exception e) {
            System.err.println("❌ Error procesando imagen: " + imageData.getName() + " - " + e.getMessage());
        }
    }
}

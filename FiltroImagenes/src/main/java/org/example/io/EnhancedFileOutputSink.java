package org.example.io;

import org.example.util.FileManager;
import org.example.validation.ValidationUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * OutputSink mejorado que crea la estructura "imagenesConFiltros"
 * y mantiene la jerarquía de subdirectorios
 */
public class EnhancedFileOutputSink implements OutputSink {
    private final File baseDirectory;
    private final File outputRootDirectory;
    private File currentImageDirectory;
    private String currentRelativePath;

    public EnhancedFileOutputSink(File baseDirectory) {
        if (baseDirectory == null) {
            throw new IllegalArgumentException("Error: el directorio base no puede ser nulo");
        }
        this.baseDirectory = baseDirectory;
        this.outputRootDirectory = new File(baseDirectory, "imagenesConFiltros");
    }

    @Override
    public synchronized void saveImage(BufferedImage image, String originalName, String filterName, String extension) throws IOException {
        if (currentImageDirectory == null) {
            throw new IllegalStateException("Debe llamar initializeForImage() primero");
        }

        if (image == null) {
            throw new IOException("Error: la imagen a guardar no puede ser nula");
        }

        if (originalName == null || originalName.trim().isEmpty()) {
            throw new IOException("Error: el nombre original no puede estar vacío");
        }

        if (extension == null || extension.trim().isEmpty()) {
            throw new IOException("Error: la extensión no puede estar vacía");
        }

        ValidationUtils.validateImage(image, originalName);
        ValidationUtils.validateOutputDirectory(currentImageDirectory);

        String filename;
        if (filterName == null) {
            filename = originalName;
        } else {
            String baseName = getBaseName(originalName);
            filename = baseName + "_" + filterName + "." + extension;
        }

        FileManager.guardarImagen(image, currentImageDirectory, filename, extension);
    }

    @Override
    public synchronized void initializeForImage(String imageName) {
        System.err.println("  🔧 Inicializando para imagen: " + imageName);

        if (imageName instanceof String && imageName.contains("|ENHANCED|")) {
            String[] parts = imageName.split("\\|ENHANCED\\|");
            if (parts.length >= 2) {
                String actualImageName = parts[0];
                String relativePath = parts[1];
                System.err.println("  🔧 Modo enhanced - Imagen: " + actualImageName + ", Ruta: " + relativePath);
                initializeForEnhancedImage(actualImageName, relativePath);
                return;
            }
        }

        // Fallback to standard initialization
        System.err.println("  🔧 Modo estándar para: " + imageName);
        initializeForStandardImage(imageName);
    }

    private void initializeForEnhancedImage(String imageName, String relativePath) {
        if (imageName == null || imageName.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: el nombre de la imagen no puede estar vacío");
        }

        try {
            ValidationUtils.validateOutputDirectory(baseDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Error: no se puede inicializar el directorio de salida - " + e.getMessage());
        }

        this.currentRelativePath = relativePath;

        // Create the subdirectory structure if needed
        File targetDirectory = outputRootDirectory;
        if (relativePath != null && !relativePath.isEmpty() && !relativePath.equals(".")) {
            targetDirectory = new File(outputRootDirectory, relativePath);
        }

        String baseName = getBaseName(imageName);
        currentImageDirectory = FileManager.crearCarpetaImagen(targetDirectory, baseName);
    }

    private void initializeForStandardImage(String imageName) {
        if (imageName == null || imageName.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: el nombre de la imagen no puede estar vacío");
        }

        try {
            ValidationUtils.validateOutputDirectory(baseDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Error: no se puede inicializar el directorio de salida - " + e.getMessage());
        }

        String baseName = getBaseName(imageName);
        currentImageDirectory = FileManager.crearCarpetaImagen(outputRootDirectory, baseName);
    }

    @Override
    public void finalizeImage() {
        currentImageDirectory = null;
        currentRelativePath = null;
    }

    /**
     * Retorna la ruta del directorio raíz de salida para stdout
     */
    public String getOutputRootPath() {
        return outputRootDirectory.getAbsolutePath();
    }

    private String getBaseName(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return "unknown";
        }

        String cleanName = filename.trim();
        System.err.println("  🔧 Procesando nombre base de: '" + cleanName + "'");

        int dotIndex = cleanName.lastIndexOf(".");
        String baseName = (dotIndex == -1) ? cleanName : cleanName.substring(0, dotIndex);

        System.err.println("  🔧 Nombre base resultante: '" + baseName + "'");
        return baseName;
    }
}

package org.example.io;

import org.example.util.FileManager;
import org.example.validation.ValidationUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileOutputSink implements org.example.io.OutputSink {
    private final File baseDirectory;
    private File currentImageDirectory;

    public FileOutputSink(File baseDirectory) {
        if (baseDirectory == null) {
            throw new IllegalArgumentException("Error: el directorio base no puede ser nulo");
        }
        this.baseDirectory = baseDirectory;
    }

    @Override
    public void saveImage(BufferedImage image, String originalName, String filterName, String extension) throws IOException {
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
    public void initializeForImage(String imageName) {
        if (imageName == null || imageName.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: el nombre de la imagen no puede estar vacío");
        }

        try {
            ValidationUtils.validateOutputDirectory(baseDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Error: no se puede inicializar el directorio de salida - " + e.getMessage());
        }

        String baseName = getBaseName(imageName);
        currentImageDirectory = FileManager.crearCarpetaImagen(baseDirectory, baseName);
    }

    @Override
    public void finalizeImage() {
        currentImageDirectory = null;
    }

    private String getBaseName(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return "unknown";
        }

        int dotIndex = filename.lastIndexOf(".");
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }
}

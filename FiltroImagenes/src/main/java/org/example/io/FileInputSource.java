package org.example.io;

import org.example.validation.ValidationUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileInputSource implements InputSource {
    private final File directory;
    private final String[] supportedExtensions = {
            ".png", ".jpg", ".jpeg", ".bmp", ".gif", ".tiff", ".tif", ".webp"
    };

    public FileInputSource(String directoryPath) {
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: la ruta del directorio no puede estar vacía");
        }
        this.directory = new File(directoryPath);
    }

    @Override
    public List<ImageData> getImages() throws IOException {
        ValidationUtils.validateDirectory(directory.getPath());

        File[] files = directory.listFiles(this::isImageFile);
        if (files == null || files.length == 0) {
            throw new IOException("No se encontraron imágenes válidas en la ruta.");
        }

        List<ImageData> images = new ArrayList<>();
        for (File file : files) {
            try {
                ValidationUtils.validateFile(file);
                ValidationUtils.validateImageFormat(file.getName());

                BufferedImage image = ImageIO.read(file);
                if (image != null) {
                    ValidationUtils.validateImage(image, file.getName());

                    String name = file.getName();
                    String extension = getExtension(name);
                    images.add(new ImageData(image, name, extension));
                } else {
                    System.err.println("⚠️  Error: no se pudo cargar la imagen (formato no válido): " + file.getName());
                }
            } catch (Exception e) {
                System.err.println("⚠️  Error leyendo imagen: " + file.getName() + " - " + e.getMessage());
            }
        }

        if (images.isEmpty()) {
            throw new IOException("Error: no se pudieron cargar imágenes válidas del directorio");
        }

        return images;
    }

    @Override
    public boolean hasImages() {
        try {
            ValidationUtils.validateDirectory(directory.getPath());
            File[] files = directory.listFiles(this::isImageFile);
            return files != null && files.length > 0;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isImageFile(File dir, String name) {
        if (name == null) {
            return false;
        }

        String lower = name.toLowerCase();
        for (String ext : supportedExtensions) {
            if (lower.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    private String getExtension(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return "png";
        }

        int dotIndex = filename.lastIndexOf(".");
        return (dotIndex == -1) ? "png" : filename.substring(dotIndex + 1);
    }
}

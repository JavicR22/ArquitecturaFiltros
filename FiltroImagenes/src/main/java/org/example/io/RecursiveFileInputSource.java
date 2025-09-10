package org.example.io;

import org.example.validation.ValidationUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * InputSource que procesa recursivamente todos los subdirectorios
 * manteniendo la estructura jerárquica de carpetas
 */
public class RecursiveFileInputSource implements InputSource {
    private final File rootDirectory;
    private final String[] supportedExtensions = {
            ".png", ".jpg", ".jpeg", ".bmp", ".gif", ".tiff", ".tif", ".webp"
    };

    public RecursiveFileInputSource(String directoryPath) {
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: la ruta del directorio no puede estar vacía");
        }
        this.rootDirectory = new File(directoryPath);
    }

    @Override
    public List<ImageData> getImages() throws IOException {
        ValidationUtils.validateDirectory(rootDirectory.getPath());

        List<ImageData> images = new ArrayList<>();
        collectImagesRecursively(rootDirectory, images);

        if (images.isEmpty()) {
            throw new IOException("Error: no se pudieron cargar imágenes válidas del directorio y subdirectorios");
        }

        return images;
    }

    @Override
    public boolean hasImages() {
        try {
            ValidationUtils.validateDirectory(rootDirectory.getPath());
            return hasImagesRecursively(rootDirectory);
        } catch (IOException e) {
            return false;
        }
    }

    private void collectImagesRecursively(File directory, List<ImageData> images) {
        try (Stream<Path> paths = Files.walk(directory.toPath())) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> isImageFile(path.toFile()))
                    .forEach(path -> {
                        try {
                            File file = path.toFile();
                            ValidationUtils.validateFile(file);
                            ValidationUtils.validateImageFormat(file.getName());

                            BufferedImage image = ImageIO.read(file);
                            if (image != null) {
                                ValidationUtils.validateImage(image, file.getName());

                                String name = file.getName();
                                String extension = getExtension(name);
                                String relativePath = getRelativePath(file);

                                images.add(new EnhancedImageData(image, name, extension, relativePath));
                            } else {
                                System.err.println("⚠️  Error: no se pudo cargar la imagen (formato no válido): " + file.getName());
                            }
                        } catch (Exception e) {
                            System.err.println("⚠️  Error leyendo imagen: " + path.getFileName() + " - " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.err.println("⚠️  Error explorando directorio: " + directory.getPath() + " - " + e.getMessage());
        }
    }

    private boolean hasImagesRecursively(File directory) {
        try (Stream<Path> paths = Files.walk(directory.toPath())) {
            return paths.filter(Files::isRegularFile)
                    .anyMatch(path -> isImageFile(path.toFile()));
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isImageFile(File file) {
        if (file == null || file.getName() == null) {
            return false;
        }

        String lower = file.getName().toLowerCase();
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

    private String getRelativePath(File file) {
        try {
            Path rootPath = rootDirectory.toPath();
            Path filePath = file.getParentFile().toPath();
            Path relativePath = rootPath.relativize(filePath);
            return relativePath.toString();
        } catch (Exception e) {
            return "";
        }
    }
}

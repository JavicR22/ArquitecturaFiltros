package org.example.validation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ValidationUtils {

    private static final long MAX_FILE_SIZE = 200 * 1024 * 1024; // 200MB
    private static final int MAX_IMAGE_DIMENSION = 10000;
    private static final int MIN_IMAGE_DIMENSION = 1;

    public static void validateDirectory(String directoryPath) throws IOException {
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            throw new IOException("Error: la ruta del directorio no puede estar vacía");
        }

        File directory = new File(directoryPath);

        if (!directory.exists()) {
            throw new IOException("Error: el directorio no existe: " + directoryPath);
        }

        if (!directory.isDirectory()) {
            throw new IOException("Error: la ruta no es un directorio válido: " + directoryPath);
        }

        if (!directory.canRead()) {
            throw new IOException("Error: no se tienen permisos de lectura en el directorio: " + directoryPath);
        }
    }

    public static void validateFile(File file) throws IOException {
        if (file == null) {
            throw new IOException("Error: el archivo no puede ser nulo");
        }

        if (!file.exists()) {
            throw new IOException("Error: el archivo no existe: " + file.getPath());
        }

        if (!file.isFile()) {
            throw new IOException("Error: la ruta no es un archivo válido: " + file.getPath());
        }

        if (!file.canRead()) {
            throw new IOException("Error: no se tienen permisos de lectura en el archivo: " + file.getPath());
        }

        if (file.length() > MAX_FILE_SIZE) {
            throw new IOException("Error: el archivo excede el tamaño máximo permitido (200MB): " + file.getPath());
        }

        if (file.length() == 0) {
            throw new IOException("Error: el archivo está vacío: " + file.getPath());
        }
    }

    public static void validateImage(BufferedImage image, String imageName) throws IOException {
        if (image == null) {
            throw new IOException("Error: no se pudo cargar la imagen o el formato no es válido: " + imageName);
        }

        int width = image.getWidth();
        int height = image.getHeight();

        if (width < MIN_IMAGE_DIMENSION || height < MIN_IMAGE_DIMENSION) {
            throw new IOException("Error: las dimensiones de la imagen son demasiado pequeñas (mínimo 1x1): " + imageName);
        }

        if (width > MAX_IMAGE_DIMENSION || height > MAX_IMAGE_DIMENSION) {
            throw new IOException("Error: las dimensiones de la imagen exceden el máximo permitido (10000x10000): " + imageName);
        }
    }

    public static void validateImageFormat(String filename) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IOException("Error: el nombre del archivo no puede estar vacío");
        }

        String[] supportedExtensions = {".png", ".jpg", ".jpeg", ".bmp", ".gif", ".tiff", ".tif", ".webp"};
        String lowerFilename = filename.toLowerCase();

        boolean isSupported = false;
        for (String ext : supportedExtensions) {
            if (lowerFilename.endsWith(ext)) {
                isSupported = true;
                break;
            }
        }

        if (!isSupported) {
            throw new IOException("Error: formato de imagen no soportado. Formatos válidos: PNG, JPG, JPEG, BMP, GIF, TIFF, WEBP");
        }
    }

    public static void validateOutputDirectory(File outputDir) throws IOException {
        if (outputDir == null) {
            throw new IOException("Error: el directorio de salida no puede ser nulo");
        }

        if (outputDir.exists() && !outputDir.isDirectory()) {
            throw new IOException("Error: la ruta de salida existe pero no es un directorio: " + outputDir.getPath());
        }

        if (!outputDir.exists()) {
            if (!outputDir.mkdirs()) {
                throw new IOException("Error: no se pudo crear el directorio de salida: " + outputDir.getPath());
            }
        }

        if (!outputDir.canWrite()) {
            throw new IOException("Error: no se tienen permisos de escritura en el directorio de salida: " + outputDir.getPath());
        }
    }

    public static void validateStdinAvailable() throws IOException {
        try {
            if (System.in.available() == 0) {
                throw new IOException("Error: no hay datos disponibles en stdin");
            }
        } catch (IOException e) {
            throw new IOException("Error: no se puede verificar la disponibilidad de stdin: " + e.getMessage());
        }
    }
}

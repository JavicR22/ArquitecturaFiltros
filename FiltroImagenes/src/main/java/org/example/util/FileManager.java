package org.example.util;

import org.example.validation.ValidationUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileManager {

    public static File crearCarpetaImagen(File directorioBase, String nombreImagen) {
        if (directorioBase == null) {
            throw new IllegalArgumentException("Error: el directorio base no puede ser nulo");
        }

        if (nombreImagen == null || nombreImagen.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: el nombre de la imagen no puede estar vacío");
        }

        try {
            ValidationUtils.validateOutputDirectory(directorioBase);
        } catch (IOException e) {
            throw new RuntimeException("Error: directorio base inválido - " + e.getMessage());
        }

        String nombreSinExtension = nombreImagen;
        int puntoIndex = nombreImagen.lastIndexOf('.');
        if (puntoIndex > 0) {
            nombreSinExtension = nombreImagen.substring(0, puntoIndex);
        }

        File carpetaImagen = new File(directorioBase, nombreSinExtension);
        if (!carpetaImagen.exists()) {
            if (!carpetaImagen.mkdirs()) {
                throw new RuntimeException("Error: no se pudo crear la carpeta de imagen: " + carpetaImagen.getPath());
            }
        }
        return carpetaImagen;
    }

    public static File crearCarpeta(File base, String nombre) {
        if (base == null) {
            throw new IllegalArgumentException("Error: el directorio base no puede ser nulo");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: el nombre de la carpeta no puede estar vacío");
        }

        File carpeta = new File(base, nombre);
        if (!carpeta.exists()) {
            if (!carpeta.mkdirs()) {
                throw new RuntimeException("Error: no se pudo crear la carpeta: " + carpeta.getPath());
            }
        }
        return carpeta;
    }

    public static void guardarImagenOriginal(BufferedImage imagen, File carpeta, String nombreOriginal, String extension) throws IOException {
        guardarImagen(imagen, carpeta, nombreOriginal, extension);
    }

    public static void guardarImagen(BufferedImage imagen, File carpeta, String nombreArchivo, String extension) throws IOException {
        if (imagen == null) {
            throw new IOException("Error: la imagen a guardar no puede ser nula");
        }

        if (carpeta == null) {
            throw new IOException("Error: la carpeta de destino no puede ser nula");
        }

        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            throw new IOException("Error: el nombre del archivo no puede estar vacío");
        }

        if (extension == null || extension.trim().isEmpty()) {
            throw new IOException("Error: la extensión no puede estar vacía");
        }

        ValidationUtils.validateImage(imagen, nombreArchivo);
        ValidationUtils.validateOutputDirectory(carpeta);

        File salida = new File(carpeta, nombreArchivo);

        BufferedImage imagenParaGuardar = imagen;
        if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
            BufferedImage rgbImage = new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_RGB);
            rgbImage.getGraphics().setColor(java.awt.Color.WHITE);
            rgbImage.getGraphics().fillRect(0, 0, imagen.getWidth(), imagen.getHeight());
            rgbImage.getGraphics().drawImage(imagen, 0, 0, null);
            rgbImage.getGraphics().dispose();
            imagenParaGuardar = rgbImage;
        }

        String[] formatosDisponibles = ImageIO.getWriterFormatNames();
        boolean formatoSoportado = false;
        for (String formato : formatosDisponibles) {
            if (formato.equalsIgnoreCase(extension)) {
                formatoSoportado = true;
                break;
            }
        }

        String formatoFinal = formatoSoportado ? extension : "png";
        if (!formatoSoportado) {
            String nuevoNombre = nombreArchivo.substring(0, nombreArchivo.lastIndexOf('.')) + ".png";
            salida = new File(carpeta, nuevoNombre);
            System.out.println("⚠️  Formato " + extension + " no soportado, guardando como PNG: " + nuevoNombre);
        }

        try {
            ImageIO.write(imagenParaGuardar, formatoFinal, salida);
        } catch (IOException e) {
            throw new IOException("Error: no se pudo guardar la imagen en " + salida.getPath() + " - " + e.getMessage());
        }
    }
}

package org.example.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Implementación de OutputSink para enviar imágenes a stdout.
 * Implementa el principio de Responsabilidad Única (SRP).
 */
public class StdoutOutputSink implements org.example.io.OutputSink {
    private boolean originalSent = false;

    @Override
    public void saveImage(BufferedImage image, String originalName, String filterName, String extension) throws IOException {
        // En modo stdout, solo enviamos la imagen original o la primera con filtro
        if (filterName == null && !originalSent) {
            // Imagen original - la enviamos tal como está
            writeImageToStdout(image, extension);
            originalSent = true;
        } else if (filterName != null && !originalSent) {
            // Primera imagen con filtro (si no se envió la original)
            writeImageToStdout(image, extension);
            originalSent = true;
        }
        // Las demás imágenes se ignoran en modo stdout para mantener la pipeline
    }

    @Override
    public void initializeForImage(String imageName) {
        originalSent = false;
    }

    @Override
    public void finalizeImage() {
        // No hay cleanup necesario para stdout
    }

    private void writeImageToStdout(BufferedImage image, String extension) throws IOException {
        // Para stdout, siempre usamos PNG para mantener calidad y transparencia
        String format = "png";

        // Si es JPEG, convertir a RGB con fondo blanco
        BufferedImage imageToWrite = image;
        if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
            BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            rgbImage.getGraphics().setColor(java.awt.Color.WHITE);
            rgbImage.getGraphics().fillRect(0, 0, image.getWidth(), image.getHeight());
            rgbImage.getGraphics().drawImage(image, 0, 0, null);
            rgbImage.getGraphics().dispose();
            imageToWrite = rgbImage;
        }

        ImageIO.write(imageToWrite, format, System.out);
        System.out.flush();
    }
}

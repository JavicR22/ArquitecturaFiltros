package org.example.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Interface que define el contrato para destinos de salida de imágenes.
 * Implementa el principio de Inversión de Dependencias (DIP).
 */
public interface OutputSink {
    /**
     * Guarda una imagen procesada
     * @param image imagen a guardar
     * @param originalName nombre original de la imagen
     * @param filterName nombre del filtro aplicado (null para imagen original)
     * @param extension extensión del archivo
     * @throws IOException si hay error al guardar
     */
    void saveImage(BufferedImage image, String originalName, String filterName, String extension) throws IOException;

    /**
     * Inicializa el sink para una nueva imagen
     * @param imageName nombre de la imagen que se va a procesar
     */
    void initializeForImage(String imageName);

    /**
     * Finaliza el procesamiento de una imagen
     */
    void finalizeImage();
}

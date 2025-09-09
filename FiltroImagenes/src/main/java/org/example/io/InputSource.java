package org.example.io;

import java.io.IOException;
import java.util.List;

/**
 * Interface que define el contrato para fuentes de entrada de imágenes.
 * Implementa el principio de Inversión de Dependencias (DIP).
 */
public interface InputSource {
    /**
     * Obtiene las imágenes de la fuente de entrada
     * @return Lista de ImageData con las imágenes y sus metadatos
     * @throws IOException si hay error al leer las imágenes
     */
    List<ImageData> getImages() throws IOException;

    /**
     * Verifica si la fuente tiene imágenes disponibles
     * @return true si hay imágenes disponibles
     */
    boolean hasImages();
}

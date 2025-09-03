package org.example.domain.ports;

import java.io.IOException;
import java.util.List;

public interface DirectoryReaderPort {
    /**
     * Lista todos los archivos en un directorio (sin incluir subdirectorios)
     * @param directoryPath Ruta del directorio
     * @return Lista de rutas completas de archivos
     * @throws IOException Si hay error al leer el directorio
     */
    List<String> listFiles(String directoryPath) throws IOException;

    /**
     * Verifica si una ruta es un directorio válido
     * @param directoryPath Ruta a verificar
     * @return true si es un directorio válido
     */
    boolean isDirectory(String directoryPath);

    /**
     * Verifica si un archivo es binario (no es archivo de texto)
     * @param filePath Ruta del archivo
     * @return true si es archivo binario
     * @throws IOException Si hay error al leer el archivo
     */
    boolean isBinaryFile(String filePath) throws IOException;
}

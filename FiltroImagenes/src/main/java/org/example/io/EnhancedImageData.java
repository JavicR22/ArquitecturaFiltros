package org.example.io;

import java.awt.image.BufferedImage;

/**
 * Extensión de ImageData que incluye información de ruta relativa
 * para mantener la estructura jerárquica de directorios
 */
public class EnhancedImageData extends ImageData {
    private final String relativePath;

    public EnhancedImageData(BufferedImage image, String name, String extension, String relativePath) {
        super(image, name, extension);
        this.relativePath = relativePath != null ? relativePath : "";
    }

    public String getRelativePath() {
        return relativePath;
    }

    public boolean isInSubdirectory() {
        return !relativePath.isEmpty() && !relativePath.equals(".");
    }
}

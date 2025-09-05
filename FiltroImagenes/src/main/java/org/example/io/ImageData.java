package org.example.io;

import java.awt.image.BufferedImage;

/**
 * Clase que encapsula una imagen con sus metadatos.
 * Implementa el principio de Responsabilidad Única (SRP).
 */
public class ImageData {
    private final BufferedImage image;
    private final String name;
    private final String extension;

    public ImageData(BufferedImage image, String name, String extension) {
        this.image = image;
        this.name = name;
        this.extension = extension;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public String getBaseName() {
        int dotIndex = name.lastIndexOf(".");
        return (dotIndex == -1) ? name : name.substring(0, dotIndex);
    }
}

package org.example.filters;

import java.awt.image.BufferedImage;

public interface ImageFilter {
    BufferedImage aplicar(BufferedImage imagen);
    String getNombre();
}
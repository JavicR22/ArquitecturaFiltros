package org.example.filters;

import org.example.validation.ValidationUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class GrayscaleFilter implements ImageFilter {
    @Override
    public BufferedImage aplicar(BufferedImage imagen) {
        if (imagen == null) {
            throw new IllegalArgumentException("Error: la imagen de entrada no puede ser nula");
        }

        try {
            ValidationUtils.validateImage(imagen, "grayscale_input");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error: imagen de entrada inválida - " + e.getMessage());
        }

        BufferedImage resultado = new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                int rgb = imagen.getRGB(x, y);

                int a = (rgb >> 24) & 0xff;
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;

                int gris = (int)(0.299 * r + 0.587 * g + 0.114 * b);

                int nuevoRgb = (a << 24) | (gris << 16) | (gris << 8) | gris;
                resultado.setRGB(x, y, nuevoRgb);
            }
        }
        return resultado;
    }

    @Override
    public String getNombre() {
        return "EscalaGrises";
    }
}

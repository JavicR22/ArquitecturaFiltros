package org.example.filters;

import org.example.validation.ValidationUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class BrightnessFilter implements ImageFilter {
    @Override
    public BufferedImage aplicar(BufferedImage imagen) {
        if (imagen == null) {
            throw new IllegalArgumentException("Error: la imagen de entrada no puede ser nula");
        }

        try {
            ValidationUtils.validateImage(imagen, "brightness_input");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error: imagen de entrada inválida - " + e.getMessage());
        }

        BufferedImage resultado = new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < imagen.getWidth(); x++) {
            for (int y = 0; y < imagen.getHeight(); y++) {
                int rgb = imagen.getRGB(x, y);

                int a = (rgb >> 24) & 0xff;
                int r = Math.min(255, ((rgb >> 16) & 0xff) + 50);
                int g = Math.min(255, ((rgb >> 8) & 0xff) + 50);
                int b = Math.min(255, (rgb & 0xff) + 50);

                resultado.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return resultado;
    }

    @Override
    public String getNombre() {
        return "Brillo";
    }
}

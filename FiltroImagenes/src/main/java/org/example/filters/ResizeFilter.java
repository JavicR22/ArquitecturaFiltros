package org.example.filters;

import org.example.validation.ValidationUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ResizeFilter implements ImageFilter {
    @Override
    public BufferedImage aplicar(BufferedImage imagen) {
        if (imagen == null) {
            throw new IllegalArgumentException("Error: la imagen de entrada no puede ser nula");
        }

        try {
            ValidationUtils.validateImage(imagen, "resize_input");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error: imagen de entrada inválida - " + e.getMessage());
        }

        int nuevoAncho = imagen.getWidth() / 2;
        int nuevoAlto = imagen.getHeight() / 2;

        if (nuevoAncho < 1 || nuevoAlto < 1) {
            throw new IllegalArgumentException("Error: la imagen es demasiado pequeña para redimensionar (resultado sería menor a 1x1)");
        }

        BufferedImage resultado = new BufferedImage(nuevoAncho, nuevoAlto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resultado.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.Src);

        g.drawImage(imagen, 0, 0, nuevoAncho, nuevoAlto, null);
        g.dispose();
        return resultado;
    }

    @Override
    public String getNombre() {
        return "ReduccionTamanio";
    }
}

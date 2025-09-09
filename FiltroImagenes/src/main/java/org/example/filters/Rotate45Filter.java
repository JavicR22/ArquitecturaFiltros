package org.example.filters;

import org.example.validation.ValidationUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Rotate45Filter implements ImageFilter {
    @Override
    public BufferedImage aplicar(BufferedImage imagen) {
        if (imagen == null) {
            throw new IllegalArgumentException("Error: la imagen de entrada no puede ser nula");
        }

        try {
            ValidationUtils.validateImage(imagen, "rotate45_input");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error: imagen de entrada inválida - " + e.getMessage());
        }

        double angulo = Math.toRadians(45);
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();

        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resultado.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.Src);

        g.setTransform(AffineTransform.getRotateInstance(angulo, ancho / 2.0, alto / 2.0));
        g.drawImage(imagen, 0, 0, null);
        g.dispose();

        return resultado;
    }

    @Override
    public String getNombre() {
        return "Rotacion45";
    }
}

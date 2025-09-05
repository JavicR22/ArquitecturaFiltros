package org.example.filters;

import org.example.validation.ValidationUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Rotate90Filter implements ImageFilter {
    @Override
    public BufferedImage aplicar(BufferedImage imagen) {
        if (imagen == null) {
            throw new IllegalArgumentException("Error: la imagen de entrada no puede ser nula");
        }

        try {
            ValidationUtils.validateImage(imagen, "rotate90_input");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error: imagen de entrada inválida - " + e.getMessage());
        }

        int ancho = imagen.getHeight();
        int alto = imagen.getWidth();

        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resultado.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setComposite(AlphaComposite.Src);

        AffineTransform transform = new AffineTransform();
        transform.translate(ancho / 2.0, alto / 2.0);
        transform.rotate(Math.toRadians(90));
        transform.translate(-imagen.getWidth() / 2.0, -imagen.getHeight() / 2.0);

        g.setTransform(transform);
        g.drawImage(imagen, 0, 0, null);
        g.dispose();

        return resultado;
    }

    @Override
    public String getNombre() {
        return "Rotacion90";
    }
}

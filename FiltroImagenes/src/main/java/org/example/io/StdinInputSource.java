package org.example.io;

import org.example.validation.ValidationUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StdinInputSource implements org.example.io.InputSource {
    private BufferedImage cachedImage;
    private boolean imageLoaded = false;

    @Override
    public List<ImageData> getImages() throws IOException {
        if (!imageLoaded) {
            loadImageFromStdin();
        }

        List<ImageData> images = new ArrayList<>();
        if (cachedImage != null) {
            images.add(new ImageData(cachedImage, "stdin_image.png", "png"));
        }
        return images;
    }

    @Override
    public boolean hasImages() {
        if (!imageLoaded) {
            try {
                loadImageFromStdin();
            } catch (IOException e) {
                return false;
            }
        }
        return cachedImage != null;
    }

    private void loadImageFromStdin() throws IOException {
        try {
            ValidationUtils.validateStdinAvailable();

            cachedImage = ImageIO.read(System.in);
            imageLoaded = true;

            if (cachedImage == null) {
                throw new IOException("Error: no se pudo leer la imagen desde stdin o el formato no es válido");
            }

            ValidationUtils.validateImage(cachedImage, "stdin_image");

        } catch (Exception e) {
            imageLoaded = true;
            cachedImage = null;
            throw new IOException("Error leyendo imagen desde stdin: " + e.getMessage(), e);
        }
    }
}

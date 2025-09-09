package org.example.util;

import org.example.decoder.Decoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public class FileConverter {
    private final Decoder decoder;

    public FileConverter(Decoder decoder) {
        if (decoder == null) {
            throw new IllegalArgumentException("El decoder no puede ser null.");
        }
        this.decoder = decoder;
    }

    public Path convertFile(Path inputFilePath) throws Exception {
        if (inputFilePath == null) {
            throw new IllegalArgumentException("La ruta del archivo no puede ser null.");
        }

        if (!Files.exists(inputFilePath)) {
            throw new IllegalArgumentException("El archivo no existe: " + inputFilePath);
        }

        byte[] fileBytes = Files.readAllBytes(inputFilePath);

        String contentAsText;
        boolean isProbablyBase64 = false;
        try {
            contentAsText = new String(fileBytes, StandardCharsets.UTF_8);
            String clean = contentAsText.replaceAll("[\\r\\n\\s]", "");
            if (clean.matches("^[A-Za-z0-9+/]*={0,2}$") && clean.length() % 4 == 0) {
                isProbablyBase64 = true;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al procesar el contenido del archivo: " + e.getMessage());
        }

        if (isProbablyBase64) {
            byte[] decodedBytes = decoder.decode(contentAsText);

            String fileName = inputFilePath.getFileName().toString();
            int dotIndex = fileName.lastIndexOf(".");
            String nameWithoutExtension = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
            String newFileName = nameWithoutExtension + "_binario.bin";

            Path outputFilePath = inputFilePath.getParent().resolve(newFileName);
            Files.write(outputFilePath, decodedBytes);

            return outputFilePath;
        } else {
            throw new IllegalArgumentException("El archivo no contiene contenido Base64 válido.");
        }
    }
}

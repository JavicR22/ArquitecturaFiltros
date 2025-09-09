package org.example.decoder;

import java.util.Base64;

public class Base64Decoder implements Decoder {

    @Override
    public byte[] decode(String encodedContent) throws Exception {
        if (encodedContent == null || encodedContent.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido no puede estar vacío.");
        }

        String cleanContent = encodedContent.replaceAll("[\\r\\n\\s]", "");

        if (!isValidBase64(cleanContent)) {
            throw new IllegalArgumentException("El contenido no es Base64 válido. Verifique que solo contenga caracteres A-Z, a-z, 0-9, +, / y =");
        }

        try {
            return Base64.getDecoder().decode(cleanContent);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error al decodificar Base64: " + e.getMessage());
        }
    }

    private boolean isValidBase64(String content) {
        if (content.length() % 4 != 0) {
            return false;
        }
        return content.matches("^[A-Za-z0-9+/]*={0,2}$");
    }
}

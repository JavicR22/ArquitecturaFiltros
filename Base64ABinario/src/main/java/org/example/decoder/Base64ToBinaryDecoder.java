package org.example.decoder;

import java.util.Base64;

public class Base64ToBinaryDecoder implements Decoder {

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
            // Decodificar Base64 a bytes
            byte[] decodedBytes = Base64.getDecoder().decode(cleanContent);

            // Convertir cada byte a su representación binaria en 0s y 1s
            StringBuilder binaryString = new StringBuilder();
            for (byte b : decodedBytes) {
                // Convertir byte a string binario de 8 bits
                String binaryByte = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
                binaryString.append(binaryByte);
            }

            // Convertir el string binario a bytes para escribir al archivo
            return binaryString.toString().getBytes("UTF-8");

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

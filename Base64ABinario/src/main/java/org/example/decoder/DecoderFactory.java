package org.example.decoder;

public class DecoderFactory {

    public static Decoder getDecoder(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de decodificador no puede estar vacío.");
        }

        if ("base64".equalsIgnoreCase(type.trim())) {
            return new Base64Decoder();
        }
        if ("base64binary".equalsIgnoreCase(type.trim())) {
            return new Base64ToBinaryDecoder();
        }
        throw new IllegalArgumentException("Tipo de decodificación no soportado: '" + type + "'. Se admite 'base64' y 'base64binary'.");
    }
}

package org.example.domain.ports;

public interface BinaryToBase64ConverterPort {
    String convertToBase64(byte[] binaryData);
    byte[] convertFromBase64(String base64Data);
}

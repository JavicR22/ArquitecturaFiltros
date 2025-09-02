package org.example.infrastructure.adapters;

import org.example.domain.ports.BinaryToBase64ConverterPort;

import java.util.Base64;
public class BinaryToBase64FileConverter implements BinaryToBase64ConverterPort {

    @Override
    public String convertToBase64(byte[] binaryData) {
        if (binaryData == null) {
            throw new IllegalArgumentException("Los datos binarios no pueden ser null");
        }
        return Base64.getEncoder().encodeToString(binaryData);
    }

    @Override
    public byte[] convertFromBase64(String base64Data) {
        if (base64Data == null) {
            throw new IllegalArgumentException("Los datos Base64 no pueden ser null");
        }
        return Base64.getDecoder().decode(base64Data);
    }

}

package org.example.decoder;

public interface Decoder {
    byte[] decode(String encodedContent) throws Exception;
}
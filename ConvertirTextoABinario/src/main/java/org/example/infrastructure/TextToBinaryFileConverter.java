package org.example.infrastructure;

import org.example.application.port.FileConverter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextToBinaryFileConverter  {

    public void convertToFile(InputStream in, String outputFilePath) throws IOException {
        byte[] binaryData = convertToBinary(in);
        Files.write(Paths.get(outputFilePath), binaryData);
    }

    public void convertToStdout(InputStream in,  String outputFilePath) throws IOException {
        byte[] binaryData = convertToBinary(in);
        Files.write(Paths.get(outputFilePath), binaryData);
        System.out.write(binaryData);
        System.out.flush();
    }

    private byte[] convertToBinary(InputStream in) throws IOException {
        byte[] data = in.readAllBytes();
        StringBuilder binaryBuilder = new StringBuilder();
        for (byte b : data) {
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF))
                    .replace(' ', '0');
            binaryBuilder.append(binaryString);
        }
        return binaryBuilder.toString().getBytes();
    }
}


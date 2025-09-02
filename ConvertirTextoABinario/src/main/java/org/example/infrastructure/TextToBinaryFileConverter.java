package org.example.infrastructure;

import org.example.application.port.FileConverter;

import java.io.*;

public class TextToBinaryFileConverter implements FileConverter {
    @Override
    public void convert(File input, File output) {
        try (BufferedReader reader = new BufferedReader(new FileReader(input));
             FileOutputStream fos = new FileOutputStream(output)) {

            int character;
            while ((character = reader.read()) != -1) {
                // convertir a binario cada caracter
                String binaryString = String.format("%8s",
                        Integer.toBinaryString(character)).replace(' ', '0');
                fos.write((binaryString + System.lineSeparator()).getBytes());
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al convertir archivo", e);
        }
    }
}

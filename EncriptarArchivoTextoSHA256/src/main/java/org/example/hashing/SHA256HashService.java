package org.example.hashing;

import org.example.IFiltro;
import org.example.io.FileNameGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256HashService implements IFiltro {
    // Para InputStream (archivos grandes o stdin)
    public String hash(InputStream inputStream) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192]; // lectura por bloques
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            return bytesToHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generando hash SHA-256", e);
        }
    }


    @Override
    public void process(String inputFilePath) throws IOException {
        Path inputFile = Paths.get(inputFilePath);

        try (InputStream inputStream = Files.newInputStream(inputFile)) {
            String hash = hash(inputStream);

            // Generar nombre de salida
            String outputFileName = FileNameGenerator.generate(
                    inputFile,
                    "EncriptadoSHA256",
                    ".txt"
            );

            Path outputFile = (inputFile.getParent() == null)
                    ? Paths.get(outputFileName)
                    : inputFile.getParent().resolve(outputFileName);

            // Guardar hash en el archivo
            Files.writeString(outputFile, hash);

            System.err.println("✅ Hash guardado en: " + outputFile.toAbsolutePath());
            System.out.println(outputFile.toAbsolutePath());
            System.out.flush();

        }
    }

    // Para byte[] (casos pequeños, más directo)
    public String hashBytes(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data);
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generando hash SHA-256", e);
        }
    }

    // Utilidad interna
    private String bytesToHex(byte[] hashBytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

}

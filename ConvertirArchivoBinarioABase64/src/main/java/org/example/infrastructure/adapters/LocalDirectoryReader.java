package org.example.infrastructure.adapters;

import org.example.domain.ports.DirectoryReaderPort;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class LocalDirectoryReader implements DirectoryReaderPort {
    @Override
    public List<String> listFiles(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);

        if (!Files.exists(path)) {
            throw new IOException("El directorio no existe: " + directoryPath);
        }

        if (!Files.isDirectory(path)) {
            throw new IOException("La ruta no es un directorio: " + directoryPath);
        }

        System.out.println("🔍 Explorando directorio: " + directoryPath);

        try (var stream = Files.list(path)) {
            List<String> files = stream
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());

            System.out.println("📁 Archivos encontrados: " + files.size());
            files.forEach(file -> System.out.println("   • " + file));

            return files;
        }
    }

    @Override
    public boolean isDirectory(String directoryPath) {
        Path path = Paths.get(directoryPath);
        return Files.exists(path) && Files.isDirectory(path);
    }

    @Override
    public boolean isBinaryFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path) || !Files.isRegularFile(path)) return false;

        // Si termina en .bin, trátalo como binario (tu caso de uso)
        String name = path.getFileName().toString().toLowerCase();
        if (name.endsWith(".bin")) return true;

        // Lee solo una muestra
        byte[] sample = Files.readAllBytes(path);
        int len = Math.min(sample.length, 8192);
        if (len == 0) return false;

        boolean hasBinarySignals = false;
        for (int i = 0; i < len; i++) {
            int ub = sample[i] & 0xFF;
            if (ub == 0) { hasBinarySignals = true; break; }
            if (ub < 32 && ub != 9 && ub != 10 && ub != 13) { hasBinarySignals = true; break; }
        }
        if (hasBinarySignals) return true;

        // Prueba UTF-8 estricto (esto sí falla si no es texto real)
        CharsetDecoder dec = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);
        try {
            dec.decode(ByteBuffer.wrap(sample, 0, len));
            return false; // texto válido
        } catch (CharacterCodingException ex) {
            return true; // binario
        }
}
}

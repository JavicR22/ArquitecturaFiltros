package org.example.processor;

import org.example.decoder.Decoder;
import org.example.io.InputSource;
import org.example.io.OutputSink;

import java.io.IOException;
import java.nio.file.Path;

public class Processor {
    private final InputSource inputSource;
    private final OutputSink outputSink;
    private final Decoder decoder;

    public Processor(InputSource inputSource, OutputSink outputSink, Decoder decoder) {
        if (inputSource == null) {
            throw new IllegalArgumentException("InputSource no puede ser null.");
        }
        if (outputSink == null) {
            throw new IllegalArgumentException("OutputSink no puede ser null.");
        }
        if (decoder == null) {
            throw new IllegalArgumentException("Decoder no puede ser null.");
        }

        this.inputSource = inputSource;
        this.outputSink = outputSink;
        this.decoder = decoder;
    }


    public void process() throws Exception {
        try {
            String content = inputSource.readContent();

            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("El archivo está vacío o no contiene datos válidos.");
            }

            byte[] decodedBytes = decoder.decode(content);

            if (decodedBytes.length == 0) {
                throw new IllegalArgumentException("La decodificación resultó en datos vacíos.");
            }

            outputSink.writeContent(decodedBytes);

        } finally {
            try {
                inputSource.close();
            } catch (IOException e) {
                System.err.println("Advertencia: Error al cerrar fuente de entrada: " + e.getMessage());
            }
            try {
                outputSink.close();
            } catch (IOException e) {
                System.err.println("Advertencia: Error al cerrar destino de salida: " + e.getMessage());
            }
        }
    }

    public Path getOutputPath() {
        return outputSink.getOutputPath();
    }
}

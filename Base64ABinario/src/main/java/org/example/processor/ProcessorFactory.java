package org.example.processor;

import org.example.decoder.Decoder;
import org.example.decoder.DecoderFactory;
import org.example.io.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ProcessorFactory {

    public static Processor createPipelineProcessor() {
        Decoder decoder = DecoderFactory.getDecoder("base64");
        InputSource inputSource = new StdinInputSource();
        OutputSink outputSink = new StdoutOutputSink();
        return new Processor(inputSource, outputSink, decoder);
    }

    public static Processor createFileProcessor(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo no puede estar vacía.");
        }

        Decoder decoder = DecoderFactory.getDecoder("base64");
        Path inputPath = Paths.get(filePath);

        String fileName = inputPath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf(".");
        String nameWithoutExtension = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String newFileName = nameWithoutExtension + "_binario.bin";
        Path outputPath = inputPath.getParent() != null ?
                inputPath.getParent().resolve(newFileName) :
                Paths.get(newFileName);

        InputSource inputSource = new FileInputSource(inputPath);
        OutputSink outputSink = new FileOutputSink(outputPath);
        return new Processor(inputSource, outputSink, decoder);
    }
}

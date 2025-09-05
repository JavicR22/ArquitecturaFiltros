package org.example.factory;

import org.example.filters.*;
import org.example.io.*;
import org.example.processor.ImageProcessor;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ProcessorFactory {

    public static ImageProcessor createFileProcessor(String directoryPath) {
        InputSource inputSource = new FileInputSource(directoryPath);
        OutputSink outputSink = new FileOutputSink(new File(directoryPath));
        List<ImageFilter> filters = createDefaultFilters();

        return new ImageProcessor(inputSource, outputSink, filters);
    }

    public static ImageProcessor createPipelineProcessor() {
        InputSource inputSource = new StdinInputSource();
        OutputSink outputSink = new StdoutOutputSink();
        List<ImageFilter> filters = createDefaultFilters();

        return new ImageProcessor(inputSource, outputSink, filters);
    }

    private static List<ImageFilter> createDefaultFilters() {
        return Arrays.asList(
                new GrayscaleFilter(),
                new ResizeFilter(),
                new BrightnessFilter(),
                new Rotate45Filter(),
                new Rotate90Filter(),
                new Rotate180Filter()
        );
    }
}

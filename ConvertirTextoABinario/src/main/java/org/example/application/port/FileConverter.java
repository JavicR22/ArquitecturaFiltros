package org.example.application.port;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileConverter {
    void convert(InputStream inputStream, String outputFilePath) throws IOException;
}
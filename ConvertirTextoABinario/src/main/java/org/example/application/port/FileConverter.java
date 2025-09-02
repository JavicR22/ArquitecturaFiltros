package org.example.application.port;

import java.io.File;

public interface FileConverter {
    void convert(File input, File output);
}
package org.example.io;

import java.io.IOException;
import java.nio.file.Path;

public interface OutputSink {
    void writeContent(byte[] content) throws IOException;
    Path getOutputPath(); // Returns null for stdout
    void close() throws IOException;
}

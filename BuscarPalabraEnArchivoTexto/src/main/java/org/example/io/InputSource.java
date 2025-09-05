package org.example.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface InputSource {
    List<Path> getInputFiles() throws IOException;
    void cleanup();
}

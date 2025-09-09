package org.example.io;

import java.io.IOException;

public interface InputSource {
    String readContent() throws IOException;
    void close() throws IOException;
}

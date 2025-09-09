package org.example.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

public class StdoutOutputSink implements OutputSink {
    private final OutputStream out;

    public StdoutOutputSink() {
        if (System.console() != null) {
            this.out = OutputStream.nullOutputStream();
        } else {
            this.out = System.out;
        }
    }

    @Override
    public void writeContent(byte[] content) throws IOException {
        out.write(content);
        out.flush();
    }

    @Override
    public Path getOutputPath() {
        return null;
    }

    @Override
    public void close() throws IOException {
    }
}

package org.example.io;

import org.example.model.SearchResult;
import java.io.IOException;

public interface OutputSink {
    void writeResult(SearchResult result) throws IOException;
}

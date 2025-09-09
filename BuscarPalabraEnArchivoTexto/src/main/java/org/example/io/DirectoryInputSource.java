package org.example.io;

import org.example.util.SearchUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DirectoryInputSource implements InputSource {
    private final Path directory;
    private final boolean includeSubdirectories;

    public DirectoryInputSource(Path directory, boolean includeSubdirectories) {
        this.directory = directory;
        this.includeSubdirectories = includeSubdirectories;
    }

    public DirectoryInputSource(Path directory) {
        this(directory, false);
    }

    @Override
    public List<Path> getInputFiles() throws IOException {
        int maxDepth = includeSubdirectories ? Integer.MAX_VALUE : 1;
        
        return SearchUtils.filterTextFiles(
            Files.walk(directory, maxDepth)
                .filter(Files::isRegularFile)
                .toList()
        );
    }

    @Override
    public void cleanup() {
        // No cleanup needed for directory input
    }
}

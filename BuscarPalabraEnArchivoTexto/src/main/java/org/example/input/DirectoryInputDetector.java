package org.example.input;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DirectoryInputDetector implements InputDetector {

    @Override
    public boolean canHandle(String[] args) {
        if (args.length != 2) return false;
        try {
            return Files.exists(Paths.get(args[1])) && Files.isDirectory(Paths.get(args[1]));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 1;
    }
}

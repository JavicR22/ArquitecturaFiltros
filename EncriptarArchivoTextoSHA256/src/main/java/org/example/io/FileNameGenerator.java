package org.example.io;

import java.io.File;
import java.nio.file.Path;

public class FileNameGenerator {


    public String generate(Path inputFile, String suffix, String newExtension) {
        File file = inputFile.toFile();
        String name = file.getName();

        int dotIndex = name.lastIndexOf(".");
        String baseName = (dotIndex == -1) ? name : name.substring(0, dotIndex);

        // si no hay extensión, usamos la nueva
        String extension = (dotIndex == -1) ? newExtension : newExtension;

        String parent = file.getParent();
        if (parent == null) {
            parent = "."; // directorio actual
        }

        return parent + File.separator + baseName + suffix + extension;
    }
}

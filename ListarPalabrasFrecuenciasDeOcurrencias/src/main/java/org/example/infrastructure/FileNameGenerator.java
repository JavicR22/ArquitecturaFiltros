package org.example.infrastructure;



import java.io.File;


public class FileNameGenerator {
    public String generate(String inputFile, String suffix, String extension) {
        File file = new File(inputFile);
        String name = file.getName();

        int dotIndex = name.lastIndexOf(".");
        String baseName = (dotIndex == -1) ? name : name.substring(0, dotIndex);

        String parent = file.getParent();
        if (parent == null) {
            parent = "."; // directorio actual
        }

        return parent + File.separator + baseName + suffix + extension;
    }
}

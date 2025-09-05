package org.example.validator;

import java.nio.file.Path;

public interface FileValidator {
    boolean isValid(Path file);
}

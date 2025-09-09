package org.example.validator;

import java.nio.file.Path;

public class ValidatorFactory {
    public static FileValidator createValidator(Path file) {
        return new TextFileValidator();
    }
}

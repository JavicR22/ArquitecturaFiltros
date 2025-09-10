package org.example.validator;

import java.nio.file.Path;

public abstract class FileValidator {
    protected static final long MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB

    public abstract boolean validate(Path path) throws ValidationException;

    protected boolean isValidFileSize(long sizeBytes) {
        return sizeBytes <= MAX_FILE_SIZE;
    }
}

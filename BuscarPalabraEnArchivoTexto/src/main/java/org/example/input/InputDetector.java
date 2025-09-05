package org.example.input;

public interface InputDetector {
    boolean canHandle(String[] args);
    int getPriority();
}

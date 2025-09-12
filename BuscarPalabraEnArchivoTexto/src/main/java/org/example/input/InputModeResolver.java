package org.example.input;

import org.example.model.InputMode;
import java.util.Arrays;
import java.util.List;

public class InputModeResolver {
    private final List<InputDetector> detectors;

    public InputModeResolver() {
        this.detectors = Arrays.asList(
                new DirectoryInputDetector(),
                new StdinInputDetector()
        );
    }

    public InputMode resolveInputMode(String[] args) {
        return detectors.stream()
                .filter(detector -> detector.canHandle(args))
                .min((d1, d2) -> Integer.compare(d1.getPriority(), d2.getPriority()))
                .map(detector -> {
                    if (detector instanceof DirectoryInputDetector) return InputMode.DIRECTORY;
                    if (detector instanceof StdinInputDetector) return InputMode.STDIN;
                    return null;
                })
                .orElse(null);
    }
}

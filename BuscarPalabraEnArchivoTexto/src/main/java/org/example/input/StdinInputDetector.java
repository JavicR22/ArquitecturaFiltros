package org.example.input;

import org.example.util.SearchUtils;

public class StdinInputDetector implements InputDetector {

    @Override
    public boolean canHandle(String[] args) {
        if (args.length != 1) {
            return false;
        }

        // If no console available, we're likely in a pipeline
        if (System.console() == null) {
            return true;
        }

        // Fallback to original check
        return SearchUtils.isStdinAvailable();
    }

    @Override
    public int getPriority() {
        return 2;
    }
}

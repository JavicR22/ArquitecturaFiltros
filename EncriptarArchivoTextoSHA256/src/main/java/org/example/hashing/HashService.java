package org.example.hashing;

import java.io.IOException;
import java.io.InputStream;

public interface HashService {
    String hash(InputStream inputStream) throws IOException;

}

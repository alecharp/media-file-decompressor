package org.lecharpentier.media.decompressor.core.extraction;

import java.security.cert.Extension;
import java.util.*;

public class DecompressorRegistry {

    private static final DecompressorRegistry INSTANCE = new DecompressorRegistry();
    public static DecompressorRegistry getInstance() {
        return INSTANCE;
    }

    private final ServiceLoader<Decompressor> decompressorLoader = ServiceLoader.load(Decompressor.class);
    private final Map<String, Decompressor> decompressors = new HashMap<>();

    private DecompressorRegistry() {
        decompressors.clear();
        decompressorLoader.reload();
        for (Decompressor decompressor : decompressorLoader) {
            decompressors.put(decompressor.getExtension(), decompressor);
        }
    }

    public Set<String> getSupportedExtensions() {
        return new HashSet<>(decompressors.keySet());
    }

    public boolean supportsExtension(String extension) {
        return decompressors.containsKey(extension);
    }

    public Decompressor getForExtension(String extension) {
        return decompressors.get(extension);
    }
}

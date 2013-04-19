package org.lecharpentier.media.decompressor.core.extraction;

import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DecompressorRegistryTest {

    private DecompressorRegistry registry = DecompressorRegistry.getInstance();

    @Test
    public void registryShouldFindDecompressors() {
        // Should find at least the bundled DummyDecompressor
        assertThat(registry.getSupportedExtensions().size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void registryShouldHaveDecompressorForDummy() {
        String extension = DummyDecompressor.EXTENSION;
        assertThat(registry.supportsExtension(extension)).isTrue();
        assertThat(registry.getForExtension(extension)).isNotNull();
    }

}

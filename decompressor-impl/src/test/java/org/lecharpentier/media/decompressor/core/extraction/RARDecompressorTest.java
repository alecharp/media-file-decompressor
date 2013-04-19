package org.lecharpentier.media.decompressor.core.extraction;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RARDecompressorTest {

    @Test
    public void registrySouldFindRarDecompressor() {
        DecompressorRegistry registry = DecompressorRegistry.getInstance();
        assertThat(registry.supportsExtension(RARDecompressor.EXTENSION));
        assertThat(registry.getForExtension(RARDecompressor.EXTENSION)).isNotNull();
    }

}

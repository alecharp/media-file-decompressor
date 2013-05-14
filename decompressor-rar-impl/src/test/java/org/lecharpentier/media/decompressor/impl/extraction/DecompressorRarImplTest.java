package org.lecharpentier.media.decompressor.impl.extraction;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public class DecompressorRarImplTest {

    private DecompressorRarImpl decompressor = new DecompressorRarImpl();

    @Rule
    public TemporaryFolder destinationDir = new TemporaryFolder();

    @Test
    public void testDecompression() throws IOException, URISyntaxException {
        String archiveName = "sample.rar";
        URI archiveURI = Thread.currentThread().getContextClassLoader().getResource(archiveName).toURI();
        Path archiveSource = Paths.get(archiveURI);
        Path archiveDestination = Paths.get(destinationDir.getRoot().getPath(), archiveName);
        Files.copy(archiveSource, archiveDestination, StandardCopyOption.REPLACE_EXISTING);
        assertThat(archiveDestination.toFile()).exists();

        decompressor.decompress(archiveDestination.toFile());

        assertThat(new File(destinationDir.getRoot(), "sample")).exists();
        assertThat(new File(destinationDir.getRoot().getPath(), "sample/chimp.jpg")).exists();
        assertThat(new File(destinationDir.getRoot().getPath(), "sample/SampleText.txt")).exists();
        assertThat(new File(destinationDir.getRoot().getPath(), "sample/subdir1/pencils.jpg")).exists();
        assertThat(new File(destinationDir.getRoot().getPath(), "sample/subdir1/subdir2/relaxcat.jpg")).exists();
    }
}

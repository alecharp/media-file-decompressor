package org.lecharpentier.media.decompressor.core.extraction;

public class RARDecompressor implements Decompressor {

    public static final String EXTENSION = ".rar";

    @Override
    public String getExtension() {
        return EXTENSION;
    }

    @Override
    public void decompress(String rootDirectoryPath) {
        System.out.println("Decompressing "+rootDirectoryPath);
    }
}

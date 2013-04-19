package org.lecharpentier.media.decompressor.core.extraction;

public interface Decompressor {

    public String getExtension();

    public void decompress(String rootDirectoryPath);

}

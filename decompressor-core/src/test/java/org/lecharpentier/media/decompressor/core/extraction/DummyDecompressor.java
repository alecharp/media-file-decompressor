package org.lecharpentier.media.decompressor.core.extraction;

import org.lecharpentier.media.decompressor.core.extraction.Decompressor;

import java.util.Scanner;

public class DummyDecompressor implements Decompressor {

    public static final String EXTENSION = ".dummy";

    @Override
    public String getExtension() {
        return EXTENSION;
    }

    @Override
    public void decompress(String rootDirectoryPath) {
        Scanner scanner = new Scanner(rootDirectoryPath);
        while(scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
        scanner.close();
    }

}

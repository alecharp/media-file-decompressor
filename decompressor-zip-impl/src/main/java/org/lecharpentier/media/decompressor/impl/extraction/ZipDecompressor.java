/*
 * Copyright 2013 Adrien Lecharpentier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lecharpentier.media.decompressor.impl.extraction;

import org.lecharpentier.media.decompressor.core.extraction.Decompressor;
import org.lecharpentier.media.decompressor.core.extraction.ExtractionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Zip archive decompressor
 *
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 * @author Olivier Croisier <olivier.croisier@gmail.com>
 */
public class ZipDecompressor implements Decompressor {

    @Override
    public String getExtension() {
        return "zip";
    }

    @Override
    public void decompress(File archiveFile) throws ExtractionException {
        verifyFileExists(archiveFile);
        File file = normalizeFile(archiveFile);
        File destinationDirectory = createDestinationDirectory(file);
        extractZipFile(file, destinationDirectory);
    }

    /**
     * Checks that the given file actually exists
     *
     * @param archiveFile The file to check
     * @throws ExtractionException Thrown if the file is null or does not exist
     */
    private void verifyFileExists(File archiveFile) throws ExtractionException {
        if (archiveFile == null || !archiveFile.exists()) {
            throw new ExtractionException("File does not exist : " + archiveFile.getPath());
        }
    }

    /**
     * Resolves the canonical path to the file, removing any unneccessary indirections.
     *
     * @param archiveFile The file to resolve
     * @return A new File with a normalized path
     * @throws ExtractionException If the path cannot be resolved
     */
    private File normalizeFile(File archiveFile) throws ExtractionException {
        File file;
        try {
            file = archiveFile.getCanonicalFile();
        } catch (IOException e) {
            throw new ExtractionException("Unable to resolve canonical path to " + archiveFile);
        }
        return file;
    }

    /**
     * Creates, if needed, the given directory the archive to be decompressed into.
     *
     * @param file The directory to create
     * @return A File pointing to the newly created directory
     */
    private File createDestinationDirectory(File file) {
        String filePath = file.getPath();
        String directoryPath = filePath.substring(0, filePath.length() - 4); // '.zip' = 4 chars
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    /**
     * Decompresses the given Zip archive into the given destination directory
     *
     * @param zipFile              The archive file to decompress
     * @param destinationDirectory The directory the archive should be decompressed into
     * @throws ExtractionException If the archive cannot be decompressed
     */
    private void extractZipFile(File zipFile, File destinationDirectory) throws ExtractionException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String entryName = normalizeEntryName(entry);
                    createEntryDestinationDirectory(destinationDirectory, entryName);
                    extractEntry(zis, entryName, destinationDirectory);
                }
                zis.closeEntry();
            }
        } catch (IOException e) {
            throw new ExtractionException("Unable to decompress " + zipFile.getPath(), e);
        }
    }

    /**
     * Computes the normalized entry name
     *
     * @param zipEntry The entry which name to compute
     * @return The normalized entry name
     */
    private String normalizeEntryName(ZipEntry zipEntry) {
        String entryName = zipEntry.getName();
        // Is this required ? Must perform cross-OS tests
        //entryName = entryName.replace('/', File.separatorChar);
        //entryName = entryName.replace('\\', File.separatorChar);
        return entryName;
    }

    /**
     * Creates the required subdirectories for the given archive entry.<br/>
     * This is required, as the entry's path from the archive root is is hardcoded in the entry name.
     *
     * @param destinationDirectory The archive's destination directory
     * @param entryName            The name of the entry, complete with its path from the archive destination directory
     */
    private void createEntryDestinationDirectory(File destinationDirectory, String entryName) {
        new File(new File(destinationDirectory, entryName).getParent()).mkdirs();
    }

    /**
     * Extracts the current entry from the zipped stream.
     *
     * @param zis                  The archive input stream; will not be closed here.
     * @param entryName            The current entry name
     * @param destinationDirectory The archive's destination directory
     * @throws IOException If the archive cannot be read, or the entry cannot be written on disk
     */
    private void extractEntry(ZipInputStream zis, String entryName, File destinationDirectory) throws IOException {
        byte[] buffer = new byte[4096];
        File destFile = new File(destinationDirectory, entryName);
        try (FileOutputStream fos = new FileOutputStream(destFile)) {
            int numBytes;
            while ((numBytes = zis.read(buffer, 0, buffer.length)) != -1) {
                fos.write(buffer, 0, numBytes);
            }
        }
    }


}
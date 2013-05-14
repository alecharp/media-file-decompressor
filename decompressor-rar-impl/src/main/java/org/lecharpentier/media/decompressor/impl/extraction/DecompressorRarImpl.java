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

import com.github.junrar.extract.ExtractArchive;
import org.lecharpentier.media.decompressor.core.extraction.Decompressor;
import org.lecharpentier.media.decompressor.core.extraction.ExtractionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public class DecompressorRarImpl implements Decompressor {

    private final static Logger LOGGER = LoggerFactory.getLogger(DecompressorRarImpl.class);

    @Override
    public String getExtension() {
        return "rar";
    }

    @Override
    public void decompress(File archiveFile) throws ExtractionException {
        verifyFileExists(archiveFile);
        File file = normalizeFile(archiveFile);
        File destination = file.getParentFile();
        extract(file, destination);
    }

    /**
     * Checks that the given file actually exists
     *
     * @param archiveFile The file to check
     * @throws ExtractionException Thrown if the file is null or does not exist
     */
    private void verifyFileExists(File archiveFile) throws ExtractionException {
        if (archiveFile == null || !archiveFile.exists()) {
            throw new ExtractionException("The file does not exist: " + archiveFile.getPath());
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
        try {
            return archiveFile.getCanonicalFile();
        } catch (IOException e) {
            throw new ExtractionException("Unable to resolve canonical path to " + archiveFile);
        }
    }

    /**
     * Proceed to the extraction of the file into the destination folder
     *
     * @param archive     the RAR file to extract
     * @param destination the folder within to extract the RAR
     * @throws ExtractionException If the archive cannot be decompressed
     */
    private void extract(File archive, File destination) throws ExtractionException {
        ExtractArchive extractArchive = new ExtractArchive();
        extractArchive.extractArchive(archive, destination);
    }
}

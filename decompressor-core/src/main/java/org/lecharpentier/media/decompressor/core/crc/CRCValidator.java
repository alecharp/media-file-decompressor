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

package org.lecharpentier.media.decompressor.core.crc;

import org.lecharpentier.media.decompressor.core.model.ArchiveResource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public class CRCValidator {

    /**
     * Confront a file to its expected CRC checksum.
     * </p>
     * The given CRC must be the Hexadecimal representation of the CRC.
     *
     * @param input the file to test
     * @param crc   the expected CRC of the file, in hexadecimal mode.
     * @throws CRCMismatchException if the CRC doesn't match the computed CRC of the file
     * @throws IOException          if there are issues about the file
     */
    public void validate(File input, String crc) throws IOException, CRCMismatchException {
        try (FileInputStream fis = new FileInputStream(input);
             BufferedInputStream inputStream = new BufferedInputStream(fis)) {
            CRC32 crc32 = new CRC32();
            int cnt;
            while ((cnt = inputStream.read()) != -1) {
                crc32.update(cnt);
            }
            if (!crc.equals(Long.toHexString(crc32.getValue()))) {
                throw new CRCMismatchException(String.format("File %s doesn't match the CRC %s.", input.getName(), crc));
            }
        }
    }

    /**
     * @see CRCValidator#validate(java.io.File, String)
     */
    public void validate(ArchiveResource resource) throws IOException, CRCMismatchException {
        validate(resource.getFile(), resource.getCrc());
    }
}

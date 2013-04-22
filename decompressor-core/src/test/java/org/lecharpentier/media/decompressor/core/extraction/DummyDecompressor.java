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

package org.lecharpentier.media.decompressor.core.extraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Olivier Croisier <olivier.croisier@gmail.com>
 */
public class DummyDecompressor implements Decompressor {

    private final static Logger LOGGER = LoggerFactory.getLogger(DummyDecompressor.class);
    public static String EXTENSION = "dummy";

    @Override
    public String getExtension() {
        return EXTENSION;
    }

    @Override
    public void decompress(File archiveFile) throws ExtractionError {
        try (Scanner scanner = new Scanner(archiveFile)) {
            while (scanner.hasNext()) {
                LOGGER.debug(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            LOGGER.warn("There is no such file {}. {}", archiveFile.getAbsolutePath(), e);
        }
    }
}

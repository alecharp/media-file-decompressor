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

import java.io.File;

/**
 * This Decompressor specifies the methods to implement in order to provide a new implement to manage an archive
 * extension.
 *
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public interface Decompressor {

    /**
     * The extension that will be managed by this implementation.
     *
     * @return the value of the extension
     */
    String getExtension();

    /**
     * The method to process the decompression of an archive file.
     *
     * @param archiveFile the archive file to decompress
     * @throws ExtractionException if there is an exception during the decompression process.
     */
    void decompress(File archiveFile) throws ExtractionException;

}

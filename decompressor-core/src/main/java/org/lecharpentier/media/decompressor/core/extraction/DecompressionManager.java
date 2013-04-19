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

import com.google.common.io.Files;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public class DecompressionManager {

    private static DecompressionManager ourInstance = null;
    private final Properties properties = new Properties();

    public static DecompressionManager getInstance() throws IOException {
        if (ourInstance == null) {
            ourInstance = new DecompressionManager();
        }
        return ourInstance;
    }

    private DecompressionManager() throws IOException {
        properties.load(DecompressionManager.class.getResourceAsStream("/decompression.properties"));
    }

    public Decompression getDecompressionImplForFile(String fileName)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = (String) properties.get("extensions." + Files.getFileExtension(fileName));
        if (className == null) {
            throw new UnsupportedOperationException("The extension of the file " + fileName + " is not supported yet.");
        }
        try {
            return (Decompression) Class.forName(className).newInstance();
        } catch (ClassCastException e) {
            throw new IncorrectImplementationException("The extension is not managed by an implementation of " +
                    "org.lecharpentier.media.decompressor.core.extraction.Decompression.", e);
        }
    }
}

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 * @author Olivier Croisier <olivier.croisier@gmail.com>
 */
public class DecompressorRegistry {

    private final static Logger LOGGER = LoggerFactory.getLogger(DecompressorRegistry.class);

    private static DecompressorRegistry ourInstance = null;
    private final Map<String, Decompressor> decompressors = new HashMap<>();

    public static DecompressorRegistry getInstance() {
        if (ourInstance == null) {
            ourInstance = new DecompressorRegistry();
        }
        return ourInstance;
    }

    private DecompressorRegistry() {
        ServiceLoader<Decompressor> decompressorServiceLoader = ServiceLoader.load(Decompressor.class);
        Iterator<Decompressor> iterator = decompressorServiceLoader.iterator();
        for (Decompressor decompressor; iterator.hasNext(); ) {
            try {
                decompressor = iterator.next();
                if (decompressor.getExtension() != null) {
                    decompressors.put(decompressor.getExtension(), decompressor);
                    LOGGER.debug("{} managed by {}.", decompressor.getExtension(), decompressor.getClass().getName());
                } else {
                    LOGGER.warn("Tried to load {}, but doesn't managed any extension.",
                            decompressor.getClass().getName());
                }
            } catch (ServiceConfigurationError error) {
                LOGGER.warn("Configuration error. {}", error.getMessage());
            }
        }
    }

    public Set<String> getSupportedExtensions() {
        return new HashSet<>(decompressors.keySet());
    }

    public boolean supportsExtension(String extension) {
        return decompressors.containsKey(extension);
    }

    public Decompressor getForExtension(String extension) {
        Decompressor decompression = decompressors.get(extension);
        if (decompression == null) {
            throw new NoSuchElementException("There is no class that can manage the extension "+extension);
        }
        return decompression;
    }
}

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

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public class DecompressionManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(DecompressionManager.class);

    private static DecompressionManager ourInstance = null;
    private final Map<String, Decompression> decompressionImplList;

    public static DecompressionManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DecompressionManager();
        }
        return ourInstance;
    }

    private DecompressionManager() {
        LOGGER.debug("test");
        ServiceLoader<Decompression> decompressionLoader = ServiceLoader.load(Decompression.class);
        decompressionImplList = Maps.newHashMap();
        Iterator<? extends Decompression> iterator = decompressionLoader.iterator();
        while (iterator.hasNext()) {
            try {
                Decompression next = iterator.next();
                LOGGER.debug("Try to load {}", next.getClass().getName());
                Decompressor annotation = next.getClass().getAnnotation(Decompressor.class);
                LOGGER.debug("Annotations found: {}", annotation);
                if (annotation != null) {
                    for (String extension : annotation.extensions()) {
                        LOGGER.debug("{} managed by {}", extension, next.getClass().getName());
                        decompressionImplList.put(extension, next);
                    }
                } else {
                    LOGGER.warn("The class {} doesn't describe the managed extensions. Please refer to Decompressor " +
                            "annotation documentation.", next.getClass().getName());
                }
            } catch (ServiceConfigurationError error) {
                LOGGER.warn("Configuration error. {}", error.getMessage());
            }
        }
    }

    public Decompression getDecompressionImplForFile(String fileName) throws ClassNotFoundException {
        String extension = Files.getFileExtension(fileName);
        Decompression decompression = decompressionImplList.get(extension);
        if (decompression == null) {
            throw new ClassNotFoundException(String.format("There is no class that manage %s extension", extension));
        }
        return decompression;
    }
}

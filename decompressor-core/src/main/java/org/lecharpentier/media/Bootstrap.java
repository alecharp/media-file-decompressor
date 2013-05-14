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

package org.lecharpentier.media;

import org.lecharpentier.media.decompressor.core.watcher.DirectoryWatcher;
import org.lecharpentier.media.decompressor.core.watcher.StandartWatchEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Main entrypoint for the decompressor program
 *
 * @author Olivier Croisier <olivier.croisier@gmail.com>
 */
public class Bootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);
    private static final String PROPERTY_PREFIX = "media.directory";

    public static void main(String[] args) throws IOException {

        String configuration = System.getProperty("org.lecharpentier.media.config.file");
        Properties applicationConfiguration = new Properties();
        if (configuration != null && Files.exists(Paths.get(configuration))) {
            applicationConfiguration.load(new FileInputStream(configuration));
        }

        for (Enumeration en = applicationConfiguration.keys(); en.hasMoreElements(); ) {
            String key = (String) en.nextElement();
            if (key.startsWith(PROPERTY_PREFIX + ".")) {
                try {
                    String directory = applicationConfiguration.getProperty(key);
                    if (Files.exists(Paths.get(directory)) && Files.isDirectory(Paths.get(directory))) {
                        DirectoryWatcher directoryWatcher = new DirectoryWatcher(
                                Paths.get(directory),
                                new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE,
                                        StandardWatchEventKinds.ENTRY_MODIFY},
                                new StandartWatchEventHandler()
                        );
                        directoryWatcher.startWatching();
                        LOGGER.info("Start watching {}", directory);
                    } else {
                        LOGGER.warn("Cannot watch {} as doesn't exist or not a directory", directory);
                    }
                } catch (IOException e) {
                    LOGGER.warn("An error occured : " + e.getMessage(), e);
                }
            }
        }
    }

}
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

package org.lecharpentier.media.decompressor.core;

import org.lecharpentier.media.decompressor.core.watcher.DirectoryWatcher;
import org.lecharpentier.media.decompressor.core.watcher.StandartWatchEventHandler;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

/**
 * Main entrypoint for the decompressor program
 * @author Olivier Croisier <olivier.croisier@gmail.com>
 */
public class Main {

    public static void main(String[] args) {
        if (args.length<1) {
            System.out.println("Usage: java -cp decompressor-core.jar <directory>");
            System.exit(0);
        }

        String directory = args[0];
        try {
            DirectoryWatcher directoryWatcher = new DirectoryWatcher(Paths.get(directory), new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE}, new StandartWatchEventHandler());
            directoryWatcher.startWatching();
        } catch (IOException e) {
            System.err.println("An error occured : "+e.getMessage());
            e.printStackTrace();
        }
    }

}
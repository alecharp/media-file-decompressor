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

package org.lecharpentier.media.decompressor.core.watcher;

import org.lecharpentier.media.decompressor.core.extraction.Decompressor;
import org.lecharpentier.media.decompressor.core.extraction.DecompressorRegistry;
import org.lecharpentier.media.decompressor.core.extraction.ExtractionException;
import org.lecharpentier.media.decompressor.core.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Standard handler called when a file operation is detected. Calls the decompressor registry to find a decompressor,
 * and uses it to decompress any compatible archive
 *
 * @author Olivier Croisier <olivier.croisier@gmail.com>
 */
public class StandartWatchEventHandler implements WatchEventHandler {

    /** A thread pool to manage decompression asynchronously */
    private final ExecutorService pool = Executors.newCachedThreadPool();

    @Override
    public void onEvent(WatchEvent.Kind eventKind, final Path eventPath) {
        pool.submit(new DecompressionJob(eventPath));
    }

    /**
     * An ansynchronous job to decompress a file
     */
    private static class DecompressionJob implements Runnable {

        private static final Logger log = LoggerFactory.getLogger(DecompressionJob.class);

        private final DecompressorRegistry decompressorRegistry = DecompressorRegistry.getInstance();
        private final Path eventPath;

        public DecompressionJob(Path eventPath) {
            this.eventPath = eventPath;
        }

        @Override
        public void run() {
            String fileName = eventPath.getFileName().toString();
            String extension = getExtension(fileName);
            File file = eventPath.toFile();

            if (decompressorRegistry.supportsExtension(extension)) {

                try {
                    FileUtils.waitForFileOperationCompletion(file, 100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    return;
                }

                try {
                    Decompressor decompressor = decompressorRegistry.getForExtension(extension);
                    decompressor.decompress(file);
                } catch (ExtractionException e) {
                    log.warn("Could not decompress "+fileName, e);
                }
            }
        }

        public String getExtension(String fileName) {
            int lastDotIndex = fileName.lastIndexOf('.');
            return lastDotIndex == -1 ? null : fileName.substring(lastDotIndex + 1);
        }
    }
}

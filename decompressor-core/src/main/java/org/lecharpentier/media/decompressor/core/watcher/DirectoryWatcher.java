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

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 * Observes a directory for changes, notifying a handler as they occur.<br/>
 *
 * @author Olivier Croisier <olivier.croisier@gmail.com>
 */
public class DirectoryWatcher {

    /** The path to the directory to watch */
    private final Path directory;
    /** The user-defined handler, called whenever a changed is detected in the watched directory */
    private final WatchEventHandler handler;
    /** Types of changes the user is interested in */
    private final WatchEvent.Kind[] eventKinds;

    /** Directory watcher */
    private WatchService watchService;
    /** Thread that waits for an event to happen in the directory, and notifies the user-defined handler */
    private EventPollerThread eventPollerThread;
    private volatile boolean watching;

    /**
     * Constructs a new DirectoryWatcher that will observe the given directory for certain events and notify the given
     * handler.
     * <p/>
     * Please note that the handler will be called synchronously. It is suggested to avoid doing long processings in the
     * handler, and rather offload the task to another thread.
     *
     * @param directory The directory to observe. Must not be null, must exist.
     * @param eventKinds Types of events that shall be detected (see {@link StandardWatchEventKinds}).
     * @param handler User-defined handler, called whenever an interesting event occurs in the watched directory. Must
     * not be null.
     * @throws IOException If the directory does not exist
     */
    public DirectoryWatcher(Path directory, WatchEvent.Kind[] eventKinds, WatchEventHandler handler) throws IOException {
        this.directory = directory;
        this.handler = handler;
        this.eventKinds = eventKinds;

        Preconditions.checkNotNull(directory);
        Preconditions.checkArgument(directory.toFile().exists(), "The following directory cannot be watched because it does not exist : " + directory);
        Preconditions.checkNotNull(handler, "The handler should not be null.");
    }

    /**
     * Start watching the directory for changes.
     *
     * @throws IOException If the underlying platform does not provide any file notification system, or the directory
     * cannot be watched.
     */
    public void startWatching() throws IOException {
        watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, eventKinds);
        watching = true;
        eventPollerThread = new EventPollerThread();
        eventPollerThread.start();
    }

    /** Eventually stops watching the directory for changes.<br/> */
    public void stopWatching() {
        watching = false;
        if (eventPollerThread != null) {
            eventPollerThread.interrupt();
        }
        eventPollerThread = null;
    }

    /** Thread that waits asynchronously for events */
    private class EventPollerThread extends Thread {

        @Override
        public void run() {
            WatchKey watchKey;
            infiniteLoop:
            while (!Thread.currentThread().isInterrupted() && watching) {

                try {
                    watchKey = watchService.take();
                } catch (InterruptedException e) {
                    break infiniteLoop;
                }

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    if (Thread.currentThread().isInterrupted()) {
                        break infiniteLoop;
                    }
                    WatchEvent<Path> pathEvent = silentlyCast(event);
                    Path absoluteEventPath = directory.resolve(pathEvent.context());
                    handler.onEvent(pathEvent.kind(), absoluteEventPath);
                }

                watchKey.reset();
            }

            try {
                // We need to close the WatchService here and not in the stopWatching() method, just in case
                // the Handler itself interrupts this thread.
                watchService.close();
            } catch (IOException ignored) {
            }

        }

        /**
         * Silently casts a {@code WatchEvent&lt;?&gt;} to {@code WatchEvent&lt;Path&gt;}
         *
         * @param event The event to cast
         * @return The cast event
         */
        @SuppressWarnings("unchecked")
        private WatchEvent<Path> silentlyCast(WatchEvent<?> event) {
            return (WatchEvent<Path>) event;
        }

    }

}

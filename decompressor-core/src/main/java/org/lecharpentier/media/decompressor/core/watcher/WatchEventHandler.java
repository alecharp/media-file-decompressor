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

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * User-defined handler called by a {@link DirectoryWatcher} when an event occurs in a watched directory
 *
 * @author Olivier Croisier <olivier.croisier@gmail.com>
 */
public interface WatchEventHandler {

    /**
     * Callback method, called whenever an interesting event occurs in a watched directory.
     *
     * @param eventKind Type of event (creation, deletion...)
     * @param eventPath Absolute path to the file or directory that triggered the event
     */
    public void onEvent(WatchEvent.Kind eventKind, Path eventPath);

}

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

package org.lecharpentier.media.decompressor.core.utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Various utilitary file-related methods
 *
 * @author Olivier Croisier <olivier.croisier@gmail.com>
 */
public final class FileUtils {

    private FileUtils() {
    }

    /**
     * Wait until the given file's size stops changing.
     * <p/>
     * This method may be useful in cases where the file creation has been detected, but the file contents is still
     * being written to disk.
     *
     * @param file The file to watch
     * @param delay delay amount to wait between two file size comparisons
     * @param unit delay unit
     * @throws InterruptedException If the process is interrupted
     */
    public static void waitForFileOperationCompletion(File file, long delay, TimeUnit unit) throws InterruptedException {
        long fileLength = file.length();
        long previousFileLength = -1;
        while (fileLength != previousFileLength) {
            unit.sleep(delay);
            previousFileLength = fileLength;
            fileLength = file.length();
        }
    }

}

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

import org.lecharpentier.media.decompressor.core.model.ArchiveResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public class SFVIterator implements Iterator<ArchiveResource>, AutoCloseable {

    private final Scanner scanner;
    private final Pattern pattern;

    public SFVIterator(String sfvFilePath) throws FileNotFoundException {
        scanner = new Scanner(new FileInputStream(sfvFilePath));
        pattern = Pattern.compile("^([^ ]+) ([^ ]{8})$");
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNextLine();
    }

    @Override
    public ArchiveResource next() {
        String s1 = scanner.nextLine();
        Matcher matcher = pattern.matcher(s1);
        if (!matcher.matches()) {
            throw new IllegalStateException(String.format("The current line (%s) mismatches the expected format.", s1));
        }
        return new ArchiveResource(matcher.group(1), matcher.group(2));
    }

    /**
     * This method does nothing in this implementation
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {
        if (scanner!=null) {
            scanner.close();
        }
    }
}

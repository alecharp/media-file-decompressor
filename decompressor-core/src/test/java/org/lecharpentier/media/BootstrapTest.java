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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public class BootstrapTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private Method method;

    @Before
    public void setWatchinOnPathVisible() throws NoSuchMethodException, IOException {
        method = Bootstrap.class.getDeclaredMethod("setWatchingOnPath", Path.class, Integer.class);
        method.setAccessible(true);
    }

    @Test
    public void simpleWatcher() throws IOException, InvocationTargetException, IllegalAccessException {
        File file = temporaryFolder.newFolder();
        method.invoke(new Object(), Paths.get(file.getAbsolutePath()), 0);
    }

    @Test
    public void recursiveWatcher() throws IOException, InvocationTargetException, IllegalAccessException {
        File file = temporaryFolder.newFolder();
        File alpha = new File(file, "alpha");
        alpha.mkdir();
        File beta = new File(alpha, "beta");
        beta.mkdir();
        method.invoke(new Object(), Paths.get(file.getAbsolutePath()), 2);
    }
}

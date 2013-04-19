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

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public class DecompressionManagerTest {

    @Test
    public void rarExtension() throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Decompression decompressionImplForFile = DecompressionManager.getInstance().getDecompressionImplForFile("toto.rar");
        assertThat(decompressionImplForFile).isInstanceOfAny(Decompression.class).isInstanceOf(DecompressionRarImpl.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedExtension() throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        DecompressionManager.getInstance().getDecompressionImplForFile("a.lecharp");
    }

    @Test(expected = ClassNotFoundException.class)
    public void classNotFoundForExtension() throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        DecompressionManager.getInstance().getDecompressionImplForFile("a.test");
    }

    @Test(expected = IncorrectImplementationException.class)
    public void invalidClassForExtension() throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        DecompressionManager.getInstance().getDecompressionImplForFile("a.drien");
    }
}

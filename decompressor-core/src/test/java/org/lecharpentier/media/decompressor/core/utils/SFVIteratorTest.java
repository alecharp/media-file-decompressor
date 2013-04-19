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

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public class SFVIteratorTest {

    @Test
    public void validSFVFile() throws FileNotFoundException {
        SFVIterator sfvIterator = getSfvIterator("media.sfv");
        int cpt = 0;
        while (sfvIterator.hasNext()) { sfvIterator.next(); cpt += 1; }
        assertThat(cpt).isEqualTo(3);
    }

    @Test(expected = IllegalStateException.class)
    public void invalidSFVFile() throws FileNotFoundException {
        SFVIterator sfvIterator = getSfvIterator("bad-media.sfv");
        while (sfvIterator.hasNext()) { sfvIterator.next(); }
    }

    private SFVIterator getSfvIterator(String fileName) throws FileNotFoundException {
        return new SFVIterator(
                SFVIteratorTest.class.getResource(
                        SFVIteratorTest.class.getSimpleName() + File.separator + fileName
                ).getPath()
        );
    }
}

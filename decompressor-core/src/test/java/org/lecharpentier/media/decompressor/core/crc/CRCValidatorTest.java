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

package org.lecharpentier.media.decompressor.core.crc;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public class CRCValidatorTest {
    @Test
    public void expectRealCRC() throws IOException, URISyntaxException, CRCMismatchException {
        File reference = new File(CRCValidatorTest.class.getResource(
                CRCValidatorTest.class.getSimpleName() + File.separator + "lorem.txt").toURI()
        );
        new CRCValidator().validate(reference, "6a31a71c");
    }

    @Test(expected = CRCMismatchException.class)
    public void invalidCRC() throws IOException, URISyntaxException, CRCMismatchException {
        File reference = new File(CRCValidatorTest.class.getResource(
                CRCValidatorTest.class.getSimpleName() + File.separator + "lorem.txt").toURI()
        );
        new CRCValidator().validate(reference, "abcd1234");
    }
}

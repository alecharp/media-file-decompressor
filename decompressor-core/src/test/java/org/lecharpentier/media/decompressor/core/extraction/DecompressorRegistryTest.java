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

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public class DecompressorRegistryTest {

    private DecompressorRegistry registry;

    @Before
    public void setUp() throws Exception {
        registry = DecompressorRegistry.getInstance();
    }

    @Test
    public void registryShouldFindDecompressors() {
        assertThat(registry.getSupportedExtensions().size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void registryShouldHaveDecompressorForDummy() throws ClassNotFoundException {
        String extension = DummyDecompressor.EXTENSION;
        assertThat(registry.supportsExtension(extension)).isTrue();
        assertThat(registry.getForExtension(extension)).isNotNull().isInstanceOf(DummyDecompressor.class);
    }

    @Test(expected = ClassNotFoundException.class)
    public void unsupportedExtension() throws ClassNotFoundException {
        assertThat(registry.supportsExtension("lecharp")).isFalse();
        registry.getForExtension("lecharp");
    }

    @Test(expected = ClassNotFoundException.class)
    public void classNotFoundForExtension() throws ClassNotFoundException {
        assertThat(registry.supportsExtension("test")).isFalse();
        registry.getForExtension("test");
    }

    @Test(expected = ClassNotFoundException.class)
    public void invalidClassForExtension() throws ClassNotFoundException {
        assertThat(registry.supportsExtension("notimplementinginterface")).isFalse();
        registry.getForExtension("notimplementinginterface");
    }
}

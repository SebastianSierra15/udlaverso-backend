package com.udlaverso.udlaversobackend.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FileNameUtilsTest {

    @Test
    void removesPathSegmentsAndUnsafeCharacters() {
        assertThat(FileNameUtils.sanitize("..\\carpeta/mi imagen#.webp"))
                .isEqualTo("mi_imagen_.webp");
    }

    @Test
    void returnsFallbackForMissingNames() {
        assertThat(FileNameUtils.sanitize(null)).isEqualTo("archivo");
        assertThat(FileNameUtils.sanitize("..")).isEqualTo("archivo");
    }
}

package com.github.bingoohuang.voucherno;


import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class StrsTest {
    @Test
    public void fixedLength() {
        assertThat(Strs.fixedLength(new StringBuilder("a"), 2).toString()).isEqualTo("a0");
        assertThat(Strs.fixedLength(new StringBuilder("ab"), 2).toString()).isEqualTo("ab");
        assertThat(Strs.fixedLength(new StringBuilder("abc"), 2).toString()).isEqualTo("ab");
    }
}
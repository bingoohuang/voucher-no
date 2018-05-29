package com.github.bingoohuang.voucherno;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static com.google.common.truth.Truth.assertThat;

public class VoucherPrefixTest {
    @Test
    public void prefix() {
        String pre = VoucherPrefix.getPrefix();
        assertThat(pre.length()).isEqualTo(3);


        LocalDate date = LocalDate.of(2018, Month.MAY, 29);
        String prefix = VoucherPrefix.getPrefix(date);
        assertThat(prefix).isEqualTo("805");
    }
}

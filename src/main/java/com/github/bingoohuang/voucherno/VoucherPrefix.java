package com.github.bingoohuang.voucherno;

import java.time.LocalDate;

public class VoucherPrefix {
    public static String getPrefix() {
        return getPrefix(LocalDate.now());
    }

    public static String getPrefix(LocalDate date) {
        return String.format("%03d", date.getYear() % 10 * 100 + date.getMonth().getValue());
    }
}

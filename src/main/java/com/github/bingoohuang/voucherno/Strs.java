package com.github.bingoohuang.voucherno;

import java.util.stream.IntStream;

public abstract class Strs {
    public static StringBuilder fixedLength(StringBuilder s, int len) {
        int diff = len - s.length();
        if (diff < 0) return s.delete(len, s.length());
        if (diff > 0) return padZero(s, diff);
        return s;
    }

    public static StringBuilder padZero(StringBuilder s, int diff) {
        IntStream.range(0, diff).forEach(x -> s.append('0'));
        return s;
    }
}

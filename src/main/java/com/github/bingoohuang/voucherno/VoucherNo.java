package com.github.bingoohuang.voucherno;

import lombok.val;
import redis.clients.jedis.JedisCommands;

import java.security.SecureRandom;

public class VoucherNo {
    private final int len;
    private final SecureRandom r = new SecureRandom();
    private final BloomFilter filter;

    public VoucherNo(JedisCommands jedis, String redisKey, int len) {
        this(jedis, 0.001, 1000000, redisKey, len);
    }

    public VoucherNo(JedisCommands jedis, double falsePositiveProbability, int expectedNumberOfElements, String redisKey, int len) {
        this.filter = new BloomFilter(falsePositiveProbability, expectedNumberOfElements, new JedisCommandsBitSet(jedis, redisKey));
        this.len = len;
    }

    public String next() {
        return next(10);
    }

    /**
     * Thread-safe method to get next random voucher no.
     *
     * @param maxTries max tries to try.
     * @return random voucher NO.
     */
    public String next(int maxTries) {
        for (int i = 1; i <= maxTries; ++i) {
            val s = new StringBuilder(32);
            s.append(Math.abs(r.nextLong()));
            String n = fixedLength(s);

            if (filter.add(n)) return n;
        }

        throw new RuntimeException("try out times");
    }

    String fixedLength(StringBuilder s) {
        int diff = len - s.length();
        if (diff < 0) {
            s.delete(len, s.length());
        } else {
            padZero(s, diff);
        }

        return s.toString();
    }

    void padZero(StringBuilder s, int diff) {
        if (diff == 0) return;

        for (int i = 0; i < diff; ++i) {
            s.append('0');
        }
    }

    public boolean contains(String no) {
        return filter.contains(no);
    }
}

package com.github.bingoohuang.voucherno;

import lombok.val;
import redis.clients.jedis.BinaryJedis;

import java.security.SecureRandom;

public class VoucherNo {
    private final int len;
    private final SecureRandom r = new SecureRandom();
    private final BloomFilter filter;

    public VoucherNo(BinaryJedis jedis, String redisKey, int len) {
        this(jedis, 0.001, 1000000, redisKey, len);
    }

    public VoucherNo(BinaryJedis jedis, double falsePositiveProbability, int expectedNumberOfElements, String redisKey, int len) {
        this.filter = new BloomFilter(falsePositiveProbability, expectedNumberOfElements, new RedisBitSet(jedis, redisKey));
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
            val sb = new StringBuilder(32).append(Math.abs(r.nextLong()));
            val s = Strs.fixedLength(sb, len).toString();
            if (filter.add(s)) return s;
        }

        throw new RuntimeException("try out times");
    }


    public boolean maybeContains(String no) {
        return filter.maybeContains(no);
    }
}

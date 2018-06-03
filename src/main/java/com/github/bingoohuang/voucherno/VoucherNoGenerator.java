package com.github.bingoohuang.voucherno;

import lombok.val;
import redis.clients.jedis.BinaryJedis;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * 前缀为yMM格式的券号生成器。
 */
public class VoucherNoGenerator implements Closeable {
    private final BinaryJedis jedis;
    private final String keyPrefix;
    private VoucherNo voucherNo;
    private String lastyMM;

    public VoucherNoGenerator(BinaryJedis jedis, String keyPrefix) {
        this.jedis = jedis;
        this.keyPrefix = keyPrefix;
    }

    public String next() {
        val yMM = VoucherPrefix.getPrefix();
        val key = keyPrefix + yMM;
        val prefixChanged = isPrefixChanged(yMM, key);
        val no = voucherNo.next();
        expireRedisKeyAtStartupOfNextMonth(prefixChanged, key);

        return yMM + no;
    }

    private boolean isPrefixChanged(String yMM, String key) {
        val prefixChanged = !yMM.equals(lastyMM);
        if (prefixChanged) {
            lastyMM = yMM;
            voucherNo = new VoucherNo(jedis, key, 9);
        }

        return prefixChanged;
    }

    private void expireRedisKeyAtStartupOfNextMonth(boolean prefixChanged, String key) {
        if (!prefixChanged) return;

        // 前缀发生变化时，设置redis中的key在下个月初过期。
        val keyBytes = key.getBytes(StandardCharsets.UTF_8);
        val nextMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1).atStartOfDay();
        val epochSecond = nextMonth.toEpochSecond(ZoneOffset.of("+8"));

        jedis.expireAt(keyBytes, epochSecond);
    }

    @Override public void close() {
        jedis.close();
    }
}

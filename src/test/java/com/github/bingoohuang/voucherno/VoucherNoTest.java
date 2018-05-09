package com.github.bingoohuang.voucherno;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static com.google.common.truth.Truth.assertThat;

@Slf4j
public class VoucherNoTest {
    static VoucherNo voucherNo;

    @BeforeClass
    public static void beforeClass() {
        EmbeddedRedis.startRedis();
        voucherNo = new VoucherNo(new Jedis("127.0.0.1", EmbeddedRedis.port), "Voucher:No", 12);
    }

    @AfterClass
    public static void afterClass() {
        EmbeddedRedis.stopRedis();
    }

    @Test
    public void test() {
        for (int i = 0; i < 100; ++i) {
            val no = voucherNo.next();
            log.info("voucher NO:{}", no);
            assertThat(no).containsMatch("\\d{12}");
            assertThat(voucherNo.contains(no)).isTrue();
        }
    }

    @Test
    public void padZero() {
        StringBuilder sb = new StringBuilder("1");
        voucherNo.padZero(sb, 10);
        assertThat(sb.toString()).isEqualTo("10000000000");

        StringBuilder s = new StringBuilder("1");
        String n = voucherNo.fixedLength(s);
        assertThat(n).isEqualTo("100000000000");
    }
}

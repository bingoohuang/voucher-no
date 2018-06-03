package com.github.bingoohuang.voucherno;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import mockit.Mock;
import mockit.MockUp;
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
        Jedis jedis = new Jedis("127.0.0.1", EmbeddedRedis.port);
        voucherNo = new VoucherNo(jedis, "Voucher:No", 12);
//        voucherNo = new VoucherNo(new Jedis(), "Voucher:No", 12);
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
            assertThat(voucherNo.maybeContains(no)).isTrue();
        }
    }

    @Test
    public void padZero() {
        StringBuilder sb = new StringBuilder("1");
        assertThat(Strs.padZero(sb, 10).toString()).isEqualTo("10000000000");

        StringBuilder s = new StringBuilder("1");
        assertThat(Strs.fixedLength(s, 12).toString()).isEqualTo("100000000000");
    }


    @Test(expected = RuntimeException.class)
    public void maxTries() {
        new MockUp<BloomFilter>() {
            @Mock
            boolean add(String element) {
                return false;
            }
        };

        voucherNo.next();
    }
}

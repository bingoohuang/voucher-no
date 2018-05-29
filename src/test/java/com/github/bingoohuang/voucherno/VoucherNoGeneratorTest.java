package com.github.bingoohuang.voucherno;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static com.google.common.truth.Truth.assertThat;

@Slf4j
public class VoucherNoGeneratorTest {
    static VoucherNoGenerator generator;

    @BeforeClass
    public static void beforeClass() {
        EmbeddedRedis.startRedis();
        Jedis jedis = new Jedis("127.0.0.1", EmbeddedRedis.port);
//        Jedis jedis = new Jedis();
        generator = new VoucherNoGenerator(jedis, "Voucher:No:");
    }

    @AfterClass
    public static void afterClass() {
        EmbeddedRedis.stopRedis();
    }

    @Test
    public void test() {
        for (int i = 0; i < 100; ++i) {
            val no = generator.next();
            log.info("voucher NO:{}", no);
            assertThat(no).containsMatch("\\d{12}");
        }
    }
}

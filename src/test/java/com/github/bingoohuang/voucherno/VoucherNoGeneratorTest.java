package com.github.bingoohuang.voucherno;

import com.github.bingoohuang.utils.redis.JedisProxy;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static com.google.common.truth.Truth.assertThat;

@Slf4j
public class VoucherNoGeneratorTest {
    static VoucherNoGenerator generator;

    @BeforeClass
    public static void beforeClass() {
        EmbeddedRedis.startRedis();
//        Jedis jedis = new Jedis("127.0.0.1", EmbeddedRedis.port);
//        Jedis jedis = new Jedis();

        val poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(5);
        poolConfig.setMaxIdle(3);
        poolConfig.setMaxWaitMillis(1000 * 10);
        poolConfig.setTestOnBorrow(true);
        val jedisPool = new JedisPool(poolConfig, "127.0.0.1", EmbeddedRedis.port, 2000);

        Jedis proxy = JedisProxy.createJedisProxy(jedisPool);
        generator = new VoucherNoGenerator(proxy, "Voucher:No:");
    }

    @AfterClass
    public static void afterClass() {
        generator.close();
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

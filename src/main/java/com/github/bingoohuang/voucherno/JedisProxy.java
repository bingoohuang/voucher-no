package com.github.bingoohuang.voucherno;

import com.github.bingoohuang.westcache.cglib.Cglibs;
import lombok.val;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

public class JedisProxy {
    public static Jedis createJedisProxy(JedisPool jedisPool) {
        // 创建JedisCommands代理，每次操作，自动从资源池中获取连接，使用完，释放回资源池
        return (Jedis) Cglibs.proxy(Jedis.class, (o1, m1, args1, p1) -> {
            val pooled = jedisPool.getResource();
            val result = m1.invoke(pooled, args1);
            switch (m1.getName()) {
                case "multi":
                    return Cglibs.proxy(Transaction.class, (o2, m2, args2, p2) -> {
                        if (m2.getName().equals("close")) pooled.close();
                        return m2.invoke(result, args2);
                    });
                case "close":
                    jedisPool.destroy();
                    break;
                default:
                    pooled.close();
            }
            return result;

        });
    }
}

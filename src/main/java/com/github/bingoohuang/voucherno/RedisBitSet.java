package com.github.bingoohuang.voucherno;


import lombok.val;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Protocol;

import java.util.Arrays;

public class RedisBitSet {
    private final BinaryJedis jedis;
    private final byte[] name;

    public RedisBitSet(BinaryJedis jedis, String name) {
        this.jedis = jedis;
        this.name = name.getBytes(MessageDigestUtils.UTF8);
    }

    public boolean add(int[] hashes) {
        val multi = jedis.multi();
        Arrays.stream(hashes).forEach(x -> multi.setbit(name, x, Protocol.BYTES_TRUE));
        return multi.exec().stream().filter(x -> x == Boolean.FALSE).count() > 0;
    }

    public boolean contains(int[] hashes) {
        val multi = jedis.multi();
        Arrays.stream(hashes).forEach(x -> multi.getbit(name, x));
        return multi.exec().stream().filter(x -> x == Boolean.FALSE).count() == 0;
    }
}
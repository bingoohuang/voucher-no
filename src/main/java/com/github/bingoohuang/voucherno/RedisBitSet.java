package com.github.bingoohuang.voucherno;


import lombok.val;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Protocol;

import java.util.List;

public class RedisBitSet {
    private BinaryJedis jedis;
    private byte[] name;

    public RedisBitSet(BinaryJedis jedis, String name) {
        this.jedis = jedis;
        this.name = name.getBytes(MessageDigestUtils.UTF8);
    }

    public int set(int[] hashes) {
        val multi = jedis.multi();
        for (int hash : hashes) {
            multi.setbit(name, hash, Protocol.BYTES_TRUE);
        }
        val result = multi.exec();

        int oks = 0;
        for (val obj : result) {
            if (obj == Boolean.FALSE) ++oks;
        }

        return oks;
    }

    public int get(int[] hashes) {
        val multi = jedis.multi();
        for (int hash : hashes) {
            multi.getbit(name, hash);
        }
        List<Object> result = multi.exec();

        int falses = 0;
        for (val obj : result) {
            if (obj == Boolean.FALSE) ++falses;
        }

        return falses;
    }
}
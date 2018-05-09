package com.github.bingoohuang.voucherno;


import redis.clients.jedis.JedisCommands;

public class JedisCommandsBitSet {
    private JedisCommands jedis;
    private String name;

    public JedisCommandsBitSet(JedisCommands jedis, String name) {
        this.jedis = jedis;
        this.name = name;
    }

    public boolean set(int bitIndex, boolean value) {
        return this.jedis.setbit(this.name, bitIndex, value);
    }

    public boolean get(int bitIndex) {
        return this.jedis.getbit(this.name, bitIndex);
    }
}
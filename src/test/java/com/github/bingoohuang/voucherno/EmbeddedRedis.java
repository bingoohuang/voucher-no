package com.github.bingoohuang.voucherno;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import redis.embedded.RedisServer;

import java.net.ServerSocket;

public class EmbeddedRedis {
    private static RedisServer redisServer;

    @SneakyThrows
    public static int getRandomPort() {
        @Cleanup val socket = new ServerSocket(0);
        return socket.getLocalPort();
    }

    public static final int port = getRandomPort();


    @SneakyThrows
    public static void startRedis() {
        redisServer = new RedisServer(EmbeddedRedis.port);
        redisServer.start();
    }

    public static void stopRedis() {
        if (redisServer != null) redisServer.stop();
    }
}

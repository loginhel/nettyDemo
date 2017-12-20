package com.hhp.netty.timeTest.nio.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeServer {

    public static void main(String... args){
        int port = 8080;

        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("NIO-MultiplexerTimeServer-%d").build();
        ExecutorService executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                50, 120L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10000),namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        executor.execute(timeServer);

        //new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }
}

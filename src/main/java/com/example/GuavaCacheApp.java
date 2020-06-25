package com.example;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GuavaCacheApp {

    private LoadingCache<byte[], byte[]> cache;

    private void logInfo(String message){
        System.out.println(message);
    }

    public void save(byte[] key, byte[] value) {
        this.cache.put(key, value);
    }

    public void delete(byte[] key) {

    }

    public void init(){
        final byte[] value = Common.randomString(1000).getBytes();
        CacheLoader<byte[], byte[]> loader = new CacheLoader<byte[], byte[]>() {
            @Override
            public byte[] load(byte[] bytes) throws Exception {
                return value;
            }
        };
        cache = CacheBuilder.newBuilder().build(loader);
    }



    public void benchmarkWrite(int count){
        long startTime = System.currentTimeMillis();
        byte[] s = Common.randomString(1000).getBytes();
        for (int i = 0; i < count; i++) {
            save(Common.toBytes(i), s);
        }
        long eps = count * 1000 / (System.currentTimeMillis() - startTime);
        logInfo("Write EPS: " + eps);
    }

    public byte[] get(byte[] key) throws ExecutionException {
        return cache.get(key);
    }

    public void benchmarkRead(int count) throws ExecutionException {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            byte[] value = get(Common.toBytes(i));
            //assert new String(value).length() == 1000;
        }
        long eps = count * 1000 / (System.currentTimeMillis() - startTime);
        logInfo("Read EPS: " + eps);
    }


    public static void main(String... args) throws IOException, ExecutionException {
        GuavaCacheApp app = new GuavaCacheApp();
        app.init();
        int count = 1000000;
        app.benchmarkWrite(count);
        app.benchmarkRead(count);
        app.benchmarkRead(count);
        System.in.read();
    }
}

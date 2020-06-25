package com.example;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Random;

public class HazelCastLocalApp {

    private Map<byte[], byte[]> cache;

    private void logInfo(String message){
        System.out.println(message);
    }

    public void delete(String key) {
        cache.remove(key.getBytes());
    }

    private void save(byte[] key, byte[] value){
        cache.put(key, value);
    }

    public void init(){
        HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();
        cache = hzInstance.getMap("data");
    }

    public void benchmarkWrite(int count){
        long startTime = System.currentTimeMillis();
        byte[] value = randomString(1000).getBytes();
        for (int i = 0; i < count; i++) {
            save(Common.toBytes(i), value);
        }
        long eps = count * 1000 / (System.currentTimeMillis() - startTime);
        logInfo("Write EPS: " + eps);
    }

    public byte[] get(byte[] key) {
        return cache.get(key);
    }

    public void benchmarkRead(int count){
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            get(Common.toBytes(i));
        }
        long eps = count * 1000 / (System.currentTimeMillis() - startTime);
        logInfo("Read EPS: " + eps);
    }

    public String randomString(int len){
        byte[] array = new byte[len];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }


    public static void main(String... args) throws IOException {
        HazelCastLocalApp app = new HazelCastLocalApp();
        app.init();
        int count = 1000000;
        app.benchmarkWrite(count);
        app.benchmarkRead(count);
        System.in.read();
    }
}

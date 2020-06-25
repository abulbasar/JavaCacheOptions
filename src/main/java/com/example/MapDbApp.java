package com.example;

import org.jetbrains.annotations.NotNull;
import org.mapdb.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapDbApp {

    private HTreeMap<byte[], byte[]> cache;

    private void logInfo(String message){
        System.out.println(message);
    }

    public void save(byte[] key, byte[] value) {
        this.cache.put(key, value);
    }

    public void delete(byte[] key) {
        cache.remove(key);
    }

    public void init(){
        DB db = DBMaker.memoryDB().make();
        cache = db.hashMap("map", Serializer.BYTE_ARRAY, Serializer.BYTE_ARRAY)
                .createOrOpen();
    }



    public void benchmarkWrite(int count){
        long startTime = System.currentTimeMillis();
        byte[] s = randomString(1000).getBytes();
        for (int i = 0; i < count; i++) {
            save(Common.toBytes(i), s);
        }
        long eps = count * 1000 / (System.currentTimeMillis() - startTime);
        logInfo("Write EPS: " + eps);
    }

    public Object get(byte[] key) {
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
        MapDbApp app = new MapDbApp();
        app.init();
        int count = 1000000;
        app.benchmarkWrite(count);
        app.benchmarkRead(count);
        System.in.read();
    }
}

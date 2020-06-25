package com.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Random;

public class HazelcastApp {
    private HazelcastInstance hzInstance;
    private Map<byte[], byte[]> cache;

    private void logInfo(String message){
        System.out.println(message);
    }

    public void save(String key, String value) {
        this.cache.put(key.getBytes(), value.getBytes());
    }

    public void delete(String key) {
        cache.remove(key.getBytes());
    }

    public void init(){
        HazelcastInstance hazelcastServer = Hazelcast.newHazelcastInstance();

        final ManagementCenterConfig manCenterCfg = new ManagementCenterConfig();
        manCenterCfg.setEnabled(true).setUrl("http://localhost:8080/mancenter");

        final ClientConfig config = new ClientConfig();
        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName("dev");
        groupConfig.setPassword("dev-pass");
        hzInstance = HazelcastClient.newHazelcastClient(config);
        cache = hzInstance.getMap("data");
    }

    public void benchmarkWrite(int count){
        long startTime = System.currentTimeMillis();
        String s = randomString(1000);
        for (int i = 0; i < count; i++) {
            save(String.valueOf(i), s);
        }
        long eps = count * 1000 / (System.currentTimeMillis() - startTime);
        logInfo("Write EPS: " + eps);
    }

    public String get(String key) {
        return new String(cache.get(key.getBytes()));
    }

    public void benchmarkRead(int count){
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            get(i + "");
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
        HazelcastApp app = new HazelcastApp();
        app.init();
        int count = 1000000;
        app.benchmarkWrite(count);
        app.benchmarkRead(count);
        System.in.read();
    }
}

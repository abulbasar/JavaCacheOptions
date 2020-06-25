package com.example;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.WriteOptions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Random;


public class RocksDdApp {

    private RocksDB db;
    private WriteOptions writeOptions;

    private void logInfo(String message){
        System.out.println(message);
    }


    public void save(byte[] key, byte[] value) throws RocksDBException {
        db.put(writeOptions, key, value);
    }

    public void delete(byte[] key) throws RocksDBException {
        db.delete(writeOptions, key);
    }

    public void init(){
        RocksDB.loadLibrary();
        final Options options = new Options();
        options.setCreateIfMissing(true);
        String name = "db";
        File dbDir = new File("/tmp/rocks-db", name);

        try {
            Files.createDirectories(dbDir.toPath());
            this.db = RocksDB.open(options, dbDir.getAbsolutePath());
        } catch(IOException | RocksDBException ex) {
            ex.printStackTrace();
        }
        logInfo("RocksDB initialized and ready to use");
        writeOptions = new WriteOptions()
                .setDisableWAL(true)
                .setNoSlowdown(false);
    }

    public void benchmarkWrite(int count){
        long startTime = System.currentTimeMillis();
        byte[] s = randomString(1000).getBytes();
        for (int i = 0; i < count; i++) {
            try {
                save(Common.toBytes(i), s);
            } catch (RocksDBException e) {
                e.printStackTrace();
            }
        }
        long eps = count * 1000 / (System.currentTimeMillis() - startTime);
        logInfo("Write EPS: " + eps);
    }

    public byte[] get(byte[] key) throws RocksDBException {
        return db.get(key);
    }



    public void benchmarkRead(int count){
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            try {
                byte[] bytes = get(Common.toBytes(i));
            } catch (RocksDBException e) {
                e.printStackTrace();
            }
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
        RocksDdApp app = new RocksDdApp();
        app.init();
        int count = 1000000;
        app.benchmarkWrite(count);
        app.benchmarkRead(count);
        System.in.read();
    }
}

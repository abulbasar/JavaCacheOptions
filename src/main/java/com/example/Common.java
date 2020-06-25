package com.example;

import java.nio.charset.Charset;
import java.util.Random;

public class Common {
    public static byte[] toBytes(int value){
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte)(value >>> (i * 8));
        }
        return bytes;
    }

    public static String randomString(int len){
        byte[] array = new byte[len];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

}

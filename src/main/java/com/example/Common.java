package com.example;

import java.util.Random;

public class Common {
    public static String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";

    public static byte[] toBytes(int value){
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte)(value >>> (i * 8));
        }
        return bytes;
    }

    public static byte[] randomBytes(int len){
        byte[] array = new byte[len];
        new Random().nextBytes(array);
        return array;
    }

    public static String randomString(int len){
        final int alphaLen = AlphaNumericString.length();
        StringBuilder stringBuilder = new StringBuilder(len);
        Random random = new Random();
        int position;
        for (int i = 0; i < len; i++) {
            position = random.nextInt(alphaLen);
            stringBuilder.append(AlphaNumericString.charAt(position));
        }
        return stringBuilder.toString();
    }

}

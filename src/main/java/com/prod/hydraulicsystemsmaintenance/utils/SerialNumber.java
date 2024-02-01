package com.prod.hydraulicsystemsmaintenance.utils;

import java.security.SecureRandom;

public class SerialNumber {
    private static String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String nums = "0123456789";
    private static String alphanum = upper + nums;
    public static String generate() {
        StringBuilder serialNumber = new StringBuilder();
        SecureRandom rand = new SecureRandom();
        for (int i = 0; i < 15; i++) {
            serialNumber.append(alphanum.charAt(rand.nextInt(alphanum.length())));
        }
        return serialNumber.toString();
    }
}

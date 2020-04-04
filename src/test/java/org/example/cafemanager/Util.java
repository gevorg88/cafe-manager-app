package org.example.cafemanager;

import java.util.Random;

public class Util {
    public static String randomString(int length) {
        String alphabet = "ABCEFGHIJKLMNOPQRSTUVWXYZ1234567890_";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * alphabet.length());
            salt.append(alphabet.charAt(index));
        }
        return salt.toString();
    }

    public static Long randomLong() {
        return (new Random()).nextLong();
    }
}

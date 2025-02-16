package com.example.spendiq.util;

import java.util.Random;

public class Utils {
    private Utils(){}

    public static String generateRandomOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}

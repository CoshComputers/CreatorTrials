package com.dsd.ct.util;

import java.util.Random;

public class ModUtilities {

    private static ModUtilities instance;
    private static final Random random = new Random();
    private ModUtilities(){    }
    public static synchronized int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public static synchronized float nextFloat() {
        return random.nextFloat();
    }

    public static synchronized double nextDouble() {
        return random.nextDouble();
    }
}

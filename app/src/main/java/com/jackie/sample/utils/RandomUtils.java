package com.jackie.sample.utils;

import java.util.Random;

/**
 * Created by Jackie on 2017/7/5.
 */
public class RandomUtils {
    private static final Random RANDOM = new Random();

    public float getRandom(float lower, float upper) {
        float min = Math.min(lower, upper);
        float max = Math.max(lower, upper);

        return getRandom(max - min) + min;
    }

    public float getRandom(float upper) {
        return RANDOM.nextFloat() * upper;
    }

    public int getRandom(int upper) {
        return RANDOM.nextInt(upper);
    }
}

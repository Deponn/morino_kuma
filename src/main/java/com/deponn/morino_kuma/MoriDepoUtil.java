package com.deponn.morino_kuma;

import net.minecraft.util.RandomSource;

public class MoriDepoUtil {
    static public double getRandomParameter(RandomSource random, double spread) {
        return 1.0 + (((random.nextDouble() * 2) - 1.0) * spread);
    }
}

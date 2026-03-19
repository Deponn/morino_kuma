package com.deponn.morino_kuma.registry;

import com.deponn.morino_kuma.behavior.MoriDepoTntBehavior;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class MorinoKumaSetups {
    public static void setupBehavior(final FMLCommonSetupEvent event) {
        MoriDepoTntBehavior.setupBehavior(event);
    }
}

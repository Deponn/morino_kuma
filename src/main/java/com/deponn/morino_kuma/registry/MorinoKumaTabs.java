package com.deponn.morino_kuma.registry;

import com.deponn.morino_kuma.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MorinoKumaTabs {
    // CreativeModeTabsのDeferred Registerの作成
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MORINO_KUMA_TAB =
            CREATIVE_MODE_TABS.register("morino_kuma_tab", () -> CreativeModeTab.builder()
                    .title(Component.literal("森のくま"))
                    .icon(() -> new ItemStack(MorinoKumaBlocks.MORI_DEPO_TNT_BLOCK.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(MorinoKumaItems.MORI_DEPO_TNT_ITEM.get());
                        output.accept(MorinoKumaItems.THROWABLE_MORI_DEPO_TNT_ITEM.get());
                    })
                    .build());
}

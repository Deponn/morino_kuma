package com.deponn.morino_kuma;

import com.deponn.morino_kuma.registry.MorinoKumaBlocks;
import com.deponn.morino_kuma.registry.MorinoKumaItems;
import com.deponn.morino_kuma.registry.MorinoKumaSetups;
import com.deponn.morino_kuma.registry.MorinoKumaTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// この値はMETA-INF/mods.tomlファイルのエントリと一致する必要があります
@Mod(Constants.MOD_ID)
public class MorinoKuma
{
    // 初期処理
    public MorinoKuma()
    {
        // eventBusの作成
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // ブロックが登録されるようにDeferred Registerをmodイベントバスに登録
        MorinoKumaBlocks.BLOCKS.register(modEventBus);
        // アイテムが登録されるようにDeferred Registerをmodイベントバスに登録
        MorinoKumaItems.ITEMS.register(modEventBus);
        // タブが登録されるようにDeferred Registerをmodイベントバスに登録
        MorinoKumaTabs.CREATIVE_MODE_TABS.register(modEventBus);
        // 挙動の登録
        modEventBus.addListener(MorinoKumaSetups::setupBehavior);

        // サーバーや他の興味のあるゲームイベントに自分自身を登録
        MinecraftForge.EVENT_BUS.register(this);
        // Forgeが設定ファイルを作成して読み込むことができるように、modのForgeConfigSpecを登録
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}

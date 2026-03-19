package com.deponn.morino_kuma;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// 設定クラス
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    // コンフィグのビルダーの作成
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // 爆発の影響半径の設定
    private  static  final ForgeConfigSpec.ConfigValue<Integer> EXPLODE_RADIUS = BUILDER.comment("影響半径").define("explodeRadius",50);
    // 爆発の密度設定
    private static final ForgeConfigSpec.ConfigValue<Double> DENSITY = BUILDER.comment("効果密度").define("density",0.2);

    // コンフィグのビルド
    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int explodeRadius;
    public static double density;


    // コンフィグの初期値の設定
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        explodeRadius = EXPLODE_RADIUS.get();
        density = DENSITY.get();
    }
}

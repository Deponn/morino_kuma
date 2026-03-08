package com.deponn.morino_kuma;

import com.deponn.morino_kuma.block.DepoTNT;
import com.deponn.morino_kuma.entity.PrimedDepoTNT;
import com.deponn.morino_kuma.item.DepoTntItem;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// この値はMETA-INF/mods.tomlファイルのエントリと一致する必要があります
@Mod(MorinoKuma.MOD_ID)
public class MorinoKuma
{
    // mod id の定義
    public static final String MOD_ID = "morino_kuma";
    // ロガーの作成
    private static final Logger LOGGER = LogUtils.getLogger();
    // ブロックのDeferred Registerの作成
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    // アイテムのDeferred Registerの作成
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    // CreativeModeTabsのDeferred Registerの作成
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    //　depo_tntという名前で新しいブロックを登録
    public static final RegistryObject<Block> DEPO_TNT = BLOCKS.register("depo_tnt", DepoTNT::new);
    //　depo_tntという名前で新しいアイテムを登録(depo_tntブロックも登録)
    public static final RegistryObject<Item> DEPO_TNT_ITEM = ITEMS.register("depo_tnt",
            () -> new BlockItem(DEPO_TNT.get(), new Item.Properties()));
    // depo_tnt_throwableという名前でアイテムを登録
    public static final RegistryObject<Item> DEPO_TNT_THROWABLE_ITEM = ITEMS.register("depo_tnt_throwable",
            () -> new DepoTntItem(new Item.Properties()));

    // 初期処理
    public MorinoKuma()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // commonSetupメソッドを登録
        modEventBus.addListener(this::commonSetup);

        // ブロックが登録されるようにDeferred Registerをmodイベントバスに登録
        BLOCKS.register(modEventBus);
        // アイテムが登録されるようにDeferred Registerをmodイベントバスに登録
        ITEMS.register(modEventBus);
        // タブが登録されるようにDeferred Registerをmodイベントバスに登録
        CREATIVE_MODE_TABS.register(modEventBus);

        // サーバーや他の興味のあるゲームイベントに自分自身を登録
        MinecraftForge.EVENT_BUS.register(this);

        // アイテムをクリエイティブタブに登録
        modEventBus.addListener(this::addCreative);

        modEventBus.addListener(this::setup);

        // Forgeが設定ファイルを作成して読み込むことができるように、modのForgeConfigSpecを登録
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // セットアップイベント(初期処理で登録する)(example関数)
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // いくつかの共通セットアップコード
        LOGGER.info("HELLO FROM COMMON SETUP");
        // 例：configクラスの値によってログを出力する
        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // 例のブロックアイテムを建築ブロックタブに追加(初期処理で登録する)
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(DEPO_TNT_ITEM);
            event.accept(DEPO_TNT_THROWABLE_ITEM);
        }
    }

    // SubscribeEventを使用して、Event Busが呼び出すメソッドを発見できるようにする(example関数)
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // サーバーが起動するときに何かを行います
        LOGGER.info("HELLO from server starting");
    }

    // EventBusSubscriberを使用して、@SubscribeEventで注釈されたクラスのすべての静的メソッドを自動的に登録(example関数)
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // いくつかのクライアントセットアップコード
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    // セットアップイベント(初期処理で登録する)
    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // ディスペンサーのカスタム挙動登録
            DispenserBlock.registerBehavior(DEPO_TNT_ITEM.get(), new DefaultDispenseItemBehavior() {
                @Override
                protected ItemStack execute(BlockSource source, ItemStack stack) {
                    Level level = source.getLevel();
                    Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
                    BlockPos pos = source.getPos().relative(dir);

                    PrimedDepoTNT tnt =  DepoTNTUtil.spawnPrimedTNT(level, pos,null);
                    if (tnt != null) {
                        // 発射方向に速度を付ける
                        RandomSource random = level.getRandom();

                        // --- 速度をランダム化 ---
                        double baseSpeed = 3.0;
                        double spread = 3.0; // 横方向に散る量（大きくするとバラける）

                        // 基準となる前方向ベクトル
                        double vx = dir.getStepX() * baseSpeed;
                        double vz = dir.getStepZ() * baseSpeed;

                        // ランダムな散りを加える
                        vx += (random.nextDouble() - 0.5) * spread;
                        vz += (random.nextDouble() - 0.5) * spread;

                        // 後ろ向きになるのを防ぐ（基準方向と逆向きなら反転）
                        if (vx * dir.getStepX() < 0) vx *= -1;
                        if (vz * dir.getStepZ() < 0) vz *= -1;

                        // 少し上向きにする
                        double vy = 0.2 + random.nextDouble() * 0.1;

                        tnt.setDeltaMovement(vx, vy, vz);
                    }
                    stack.shrink(1);
                    return stack;
                }
            });
        });
    }
}

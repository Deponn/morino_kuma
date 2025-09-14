package com.deponn.morino_kuma;

import com.deponn.morino_kuma.block.DepoTNT;
import com.deponn.morino_kuma.item.DepoTntItem;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
// すべての参照で共通の場所にmod idを定義します
    public static final String MOD_ID = "morino_kuma";
// slf4jロガーを直接参照します
    private static final Logger LOGGER = LogUtils.getLogger();
// "morino_kuma"名前空間の下で登録されるすべてのブロックを保持するDeferred Registerを作成します
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
// "morino_kuma"名前空間の下で登録されるすべてのアイテムを保持するDeferred Registerを作成します
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
// "morino_kuma"名前空間の下で登録されるすべてのCreativeModeTabsを保持するDeferred Registerを作成します
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

// 名前空間とパスを組み合わせたid "morino_kuma:depo_tnt"で新しいブロックを作成します
public static final RegistryObject<Block> DEPO_TNT = BLOCKS.register("depo_tnt", DepoTNT::new);
    // 名前空間とパスを組み合わせたid "morino_kuma:depo_tnt"で新しいBlockItemを作成します
    public static final RegistryObject<Item> DEPO_TNT_ITEM = ITEMS.register("depo_tnt",
            () -> new DepoTntItem(new Item.Properties()));

    public MorinoKuma(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

// modloading用のcommonSetupメソッドを登録します
        modEventBus.addListener(this::commonSetup);

// ブロックが登録されるようにDeferred Registerをmodイベントバスに登録します
        BLOCKS.register(modEventBus);
// アイテムが登録されるようにDeferred Registerをmodイベントバスに登録します
        ITEMS.register(modEventBus);
// タブが登録されるようにDeferred Registerをmodイベントバスに登録します
        CREATIVE_MODE_TABS.register(modEventBus);

// サーバーや他の興味のあるゲームイベントに自分自身を登録します
        MinecraftForge.EVENT_BUS.register(this);

// アイテムをクリエイティブタブに登録します
        modEventBus.addListener(this::addCreative);

// Forgeが設定ファイルを作成して読み込むことができるように、modのForgeConfigSpecを登録します
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // いくつかの共通セットアップコード
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

// 例のブロックアイテムを建築ブロックタブに追加します
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(DEPO_TNT_ITEM);
    }

// SubscribeEventを使用して、Event Busが呼び出すメソッドを発見できるようにします
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // サーバーが起動するときに何かを行います
        LOGGER.info("HELLO from server starting");
    }

// EventBusSubscriberを使用して、@SubscribeEventで注釈されたクラスのすべての静的メソッドを自動的に登録します
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
}

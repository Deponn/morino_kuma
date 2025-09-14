package com.example.examplemod;

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
@Mod(ExampleMod.MODID)
public class ExampleMod
{
// すべての参照で共通の場所にmod idを定義します
    public static final String MODID = "examplemod";
// slf4jロガーを直接参照します
    private static final Logger LOGGER = LogUtils.getLogger();
// "examplemod"名前空間の下で登録されるすべてのブロックを保持するDeferred Registerを作成します
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
// "examplemod"名前空間の下で登録されるすべてのアイテムを保持するDeferred Registerを作成します
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
// "examplemod"名前空間の下で登録されるすべてのCreativeModeTabsを保持するDeferred Registerを作成します
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

// 名前空間とパスを組み合わせたid "examplemod:example_block"で新しいブロックを作成します
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
// 名前空間とパスを組み合わせたid "examplemod:example_block"で新しいBlockItemを作成します
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

// id "examplemod:example_id"で新しい食品アイテムを作成します、栄養価1、満腹度2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build())));

// 例のアイテム用のid "examplemod:example_tab"のクリエイティブタブを作成します、戦闘タブの後に配置されます
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public ExampleMod(FMLJavaModLoadingContext context)
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
            event.accept(EXAMPLE_BLOCK_ITEM);
    }

// SubscribeEventを使用して、Event Busが呼び出すメソッドを発見できるようにします
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // サーバーが起動するときに何かを行います
        LOGGER.info("HELLO from server starting");
    }

// EventBusSubscriberを使用して、@SubscribeEventで注釈されたクラスのすべての静的メソッドを自動的に登録します
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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

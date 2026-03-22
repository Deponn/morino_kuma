package com.deponn.morino_kuma.registry;

import com.deponn.morino_kuma.Constants;
import com.deponn.morino_kuma.item.MoriDepoGrenadeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MorinoKumaItems {
    // アイテムのDeferred Registerの作成
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    //　depo_tntという名前で新しいアイテムを登録(depo_tntブロックも登録)
    public static final RegistryObject<Item> MORI_DEPO_TNT_ITEM = ITEMS.register("mori_depo_tnt_item",
            () -> new BlockItem(MorinoKumaBlocks.MORI_DEPO_TNT_BLOCK.get(), new Item.Properties()));
    // depo_tnt_throwableという名前でアイテムを登録
    public static final RegistryObject<Item> MORI_DEPO_GRENADE_ITEM = ITEMS.register("mori_depo_grenade_item",
            () -> new MoriDepoGrenadeItem(new Item.Properties()));
}

package com.deponn.morino_kuma.registry;

import com.deponn.morino_kuma.Constants;
import com.deponn.morino_kuma.block.MoriDepoTntBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MorinoKumaBlocks {
    // ブロックのDeferred Registerの作成
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);

    //　depo_tntという名前で新しいブロックを登録
    public static final RegistryObject<Block> MORI_DEPO_TNT_BLOCK = BLOCKS.register("mori_depo_tnt_block", MoriDepoTntBlock::new);

}

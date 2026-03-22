package com.deponn.morino_kuma.behavior;

import com.deponn.morino_kuma.entity.PrimedMoriDepoTnt;
import com.deponn.morino_kuma.registry.MorinoKumaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

public class MoriDepoTntBehavior {
    public static void setupBehavior(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // ディスペンサーのカスタム挙動登録
            DispenserBlock.registerBehavior(MorinoKumaItems.MORI_DEPO_TNT_ITEM.get(), new DefaultDispenseItemBehavior() {
                @Override
                protected @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
                    Level level = source.getLevel();
                    Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
                    // 発射口の位置
                    BlockPos pos = source.getPos().relative(dir);
                    Vec3 posVec = new Vec3(
                            pos.getX() + 0.5,
                            pos.getY(),
                            pos.getZ() + 0.5
                    );
                    // 上方向に速度あり
                    Vec3 dirVec = new Vec3(
                            0,
                            1,
                            0
                    );

                    PrimedMoriDepoTnt.launchPrimedTnt(level,posVec,dirVec, 0.2,0.0,0.1,null);
                    stack.shrink(1);
                    return stack;
                }
            });
        });
    }
}

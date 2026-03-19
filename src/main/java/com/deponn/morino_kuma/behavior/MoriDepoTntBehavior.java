package com.deponn.morino_kuma.behavior;

import com.deponn.morino_kuma.entity.PrimedMoriDepoTnt;
import com.deponn.morino_kuma.registry.MorinoKumaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class MoriDepoTntBehavior {
    public static void setupBehavior(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // ディスペンサーのカスタム挙動登録
            DispenserBlock.registerBehavior(MorinoKumaItems.MORI_DEPO_TNT_ITEM.get(), new DefaultDispenseItemBehavior() {
                @Override
                protected ItemStack execute(BlockSource source, ItemStack stack) {
                    Level level = source.getLevel();
                    Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
                    BlockPos pos = source.getPos().relative(dir);

                    PrimedMoriDepoTnt tnt =  PrimedMoriDepoTnt.spawnPrimedTNT(level, pos,null);
                    if (tnt != null) {
                        // 発射方向に速度を付ける
                        double baseSpeed = 0.1;
                        double vx = dir.getStepX() * baseSpeed;
                        double vy = baseSpeed * 0.5;
                        double vz = dir.getStepZ() * baseSpeed;

                        // --- 速度をランダム化 ---
                        RandomSource random = level.getRandom();
                        double spread = 0.5;
                        double dx = random.nextDouble() + spread;
                        double dy = random.nextDouble() + spread;
                        double dz = random.nextDouble() + spread;

                        tnt.setDeltaMovement(vx * dx, vy * dy, vz * dz);
                    }
                    stack.shrink(1);
                    return stack;
                }
            });
        });
    }
}

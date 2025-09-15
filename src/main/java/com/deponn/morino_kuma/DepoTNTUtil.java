package com.deponn.morino_kuma;

import com.deponn.morino_kuma.entity.PrimedDepoTNT;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;


public class DepoTNTUtil {
    public static PrimedDepoTNT spawnPrimedTNT(Level level, BlockPos pos, @Nullable LivingEntity owner) {
        if (level.isClientSide) return null;

        PrimedDepoTNT tnt = new PrimedDepoTNT(
                level,
                pos.getX(),
                pos.getY() + 1.5, // プレイヤーの頭上に出す
                pos.getZ(),
                owner
        );
        level.addFreshEntity(tnt);

        // 起爆音
        level.playSound(null,pos.getX(), pos.getY(), pos.getZ(),
                SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);

        return tnt;
    }
}

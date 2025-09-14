package com.deponn.morino_kuma.block;

import com.deponn.morino_kuma.entity.PrimedDepoTNT;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class DepoTNT extends Block {

    public DepoTNT() {
        super(BlockBehaviour.Properties.copy(Blocks.TNT));
    }

    /**
     * ブロックが設置されたときに呼ばれる。設置＝起爆したい要件に対応。
     */
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        // 既に同じブロックが置かれている場合は無視（更新による重複処理防止）
        if (!level.isClientSide && !state.is(oldState.getBlock())) {
            prime(level, pos, null);
            level.removeBlock(pos, false);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            // TNTを爆発させる
            prime(level, pos, player);
            level.removeBlock(pos, false);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * 近隣の爆発で壊された（chain reaction）ときに呼ばれるメソッド。
     * ここでも PrimedTnt を生成して連鎖させる。
     */
    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            prime(level, pos, null);
        }
    }

    /**
     * PrimedTnt を生成して fuse を設定するヘルパー。
     * fuse はティック数（20ティック＝1秒）で指定。
     */
    private void prime(Level level, BlockPos pos, LivingEntity igniter) {
        PrimedDepoTNT customTnt = new PrimedDepoTNT(level,
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                igniter);
        level.addFreshEntity(customTnt);
        level.playSound(null, pos, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}

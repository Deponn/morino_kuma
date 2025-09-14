package com.deponn.morino_kuma.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class PrimedDepoTNT extends PrimedTnt {

    public PrimedDepoTNT(Level level, double x, double y, double z, LivingEntity owner) {
        super(level, x, y, z, owner);
    }

    @Override
    public void explode() {
        if (!level().isClientSide) {
            Explosion explosion = new Explosion(
                    level(),
                    this, // 爆発の原因
                    null,
                    null,
                    getX(), getY(), getZ(),
                    0.0F, // 爆発の強さ
                    false, // fire
                    Explosion.BlockInteraction.KEEP // ブロックを壊さない
            );
            explosion.explode();
            explosion.finalizeExplosion(true);
            if (level() instanceof ServerLevel serverLevel) {
                serverLevel.playSound(
                        null,
                        getX(), getY(), getZ(),
                        SoundEvents.GENERIC_EXPLODE,
                        SoundSource.BLOCKS,
                        4.0F,  // 音量
                        1.0F   // ピッチ
                );
            }

            // 森を生成
            generateForest(level(), blockPosition(), 50);

            discard(); // エンティティ削除
        }
    }

    private void generateForest(Level level, BlockPos center, int radius) {
        RandomSource random = level.random;

        for (int i = 0; i < 1000; i++) { // 200スポット試行
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;
            BlockPos basePos = center.offset(dx, 0, dz);

            // その位置の地表
            BlockPos topPos = level.getHeightmapPos(
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                    basePos
            );

            BlockPos ground = topPos.below();

            if (level.isEmptyBlock(topPos) && !level.getBlockState(ground).is(Blocks.OAK_LEAVES)) {
                // 草ブロックを置く
                level.setBlockAndUpdate(ground, Blocks.GRASS_BLOCK.defaultBlockState());

                // 30%で草に変える
                if (random.nextFloat() < 0.3f) {
                    level.setBlock(topPos, Blocks.GRASS.defaultBlockState(), 3);
                }

                // 10%で木を生やす
                if (random.nextFloat() < 0.1f) {
                    growTree(level, ground.above(), random);
                }
            }
        }
    }

    private void growTree(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return; // サーバー側でのみ実行
        }

        Holder<ConfiguredFeature<?, ?>> holder =
                serverLevel.registryAccess()
                        .registryOrThrow(Registries.CONFIGURED_FEATURE)
                        .getHolder(TreeFeatures.OAK)
                        .orElse(null);

        if (holder != null) {
            ChunkGenerator generator = serverLevel.getChunkSource().getGenerator();
            holder.value().place(serverLevel, generator, random, pos);
        }
    }
}

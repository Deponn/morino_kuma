package com.deponn.morino_kuma.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class PrimedDepoTNT extends PrimedTnt {

    public PrimedDepoTNT(Level level, double x, double y, double z, LivingEntity owner) {
        super(level, x, y, z, owner);
    }

    // 爆発時
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
                        2.0F,  // 音量
                        1.0F   // ピッチ
                );
            }

            // 森を生成
            generateForest(level(), blockPosition(), 50);

            discard(); // エンティティ削除
        }
    }

    // 森を作る挙動
    private void generateForest(Level level, BlockPos center, int radius) {
        RandomSource random = level.random;

        for (int i = 0; i < 2000; i++) { // 200スポット試行
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;
            BlockPos basePos = center.offset(dx, 0, dz);

            // その位置の地表
            BlockPos topPos = level.getHeightmapPos(
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                    basePos
            );

            BlockPos ground = topPos.below();

            if (level.isEmptyBlock(topPos) && isSolidGround(level.getBlockState(ground))) {
                // 草ブロックを置く
                level.setBlockAndUpdate(ground, Blocks.GRASS_BLOCK.defaultBlockState());

                // 30%で草に変える
                if (random.nextFloat() < 0.3f) {
                    level.setBlock(topPos, Blocks.GRASS.defaultBlockState(), 3);
                }

                // 10%で木を生やす
                if (random.nextFloat() < 0.03f) {
                    growTree(level, ground.above(), random);
                }
            }
        }
    }

    private boolean isSolidGround(BlockState state) {
        if (state == null) return false;

        // 空気は当然地面じゃない
        if (state.isAir()) return false;
        // 葉っぱや作物などの柔らかいものは false
        if (state.is(BlockTags.LEAVES)) return false;   // 葉っぱ
        if (state.is(BlockTags.CROPS)) return false;   // 作物
        if (state.is(Blocks.WATER)) return false;       // 水
        if (state.is(Blocks.LAVA)) return false;        // 溶岩
        if (state.is(Blocks.SNOW)) return false;        // 雪の層
        if (state.is(Blocks.CACTUS)) return false;      // サボテン
        if (state.is(Blocks.ICE) || state.is(Blocks.PACKED_ICE) || state.is(Blocks.BLUE_ICE)) return false; // 氷系
        if (state.is(Blocks.MAGMA_BLOCK)) return false; // マグマブロック
        return true;
    }

    // 木をはやす
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

package com.deponn.morino_kuma.item;

import com.deponn.morino_kuma.DepoTNTUtil;
import com.deponn.morino_kuma.entity.PrimedDepoTNT;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DepoTntItem extends Item {

    public DepoTntItem(Properties properties) {
        super(properties);
    }

    // 使用時に即時着火したものが飛ぶ
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // 空中右クリック時にPrimedDepoTNTをスポーン
            PrimedDepoTNT tnt =  DepoTNTUtil.spawnPrimedTNT(level,player.getOnPos(), player);
            if (tnt != null) {
                // 発射方向に速度を付ける
                RandomSource random = level.getRandom();
                // プレイヤーが向いている方向
                Vec3 look = player.getLookAngle();

                // 2.0～4.0 のランダムスピード
                double speed = 2.0 + random.nextDouble() * 2.0;

                // 少し上向きにする（真上に飛びすぎない程度）
                Vec3 direction = new Vec3(look.x, look.y + 0.1, look.z).normalize();

                // 実際の速度ベクトルを計算
                Vec3 velocity = direction.scale(speed);

                // TNT に速度をセット
                tnt.setDeltaMovement(velocity);
            }
            // アイテムを1つ消費
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}

package com.deponn.morino_kuma.item;

import com.deponn.morino_kuma.DepoTNTUtil;
import com.deponn.morino_kuma.entity.PrimedDepoTNT;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DepoTntItem extends Item {

    public DepoTntItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // 空中右クリック時にPrimedDepoTNTをスポーン
            DepoTNTUtil.spawnPrimedTNT(level,player.getOnPos(), player);

            // アイテムを1つ消費
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}

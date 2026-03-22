package com.deponn.morino_kuma.item;

import com.deponn.morino_kuma.entity.PrimedMoriDepoTnt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MoriDepoGrenadeItem extends Item {

    public MoriDepoGrenadeItem(Properties properties) {
        super(properties);
    }

    // 使用時に即時着火したものが飛ぶ
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            // プレイヤーの顔当たりの位置
            BlockPos pos =  player.getOnPos();
            Vec3 vecPos = new Vec3(
                    pos.getX(),
                    pos.getY() + 1.5,
                    pos.getZ()
            );
            // プレイヤーが向いている方向
            Vec3 dirVec = player.getLookAngle();
            Vec3 fixedDirVec = new Vec3(
                    dirVec.x,
                    dirVec.y + 0.1,
                    dirVec.z
            );
            // 空中右クリック時にPrimedDepoTNTをスポーン
            PrimedMoriDepoTnt.launchPrimedTnt(level,vecPos,fixedDirVec,3.0,1.0,0.0,player);
            // アイテムを1つ消費
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}

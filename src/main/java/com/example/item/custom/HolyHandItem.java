package com.example.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class HolyHandItem extends Item{
    public HolyHandItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);

        if(!level.isClientSide()) {
            player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 60 * 20, 2));
            player.sendOverlayMessage(Component.literal("§6La mano divina te ha elegido"));
        }
        return InteractionResult.SUCCESS;
    }
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.literal("§7Clic derecho: te da fuerza 3 por un minuto"));
        tooltipAdder.accept(Component.literal("§8Lección 3 - ítem personalizado"));
        super.appendHoverText(stack, context, display, tooltipAdder, flag);
    }
}

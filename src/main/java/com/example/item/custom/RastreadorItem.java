package com.example.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.BuiltInRegistries;

public class RastreadorItem extends Item {

    public RastreadorItem(Properties properties) {super(properties);}

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();

        // - Inspeccionar: no cambiar bloque, solo mostrar state.getBlock().getName() al jugador.

        if (!level.isClientSide() && player != null) {
            Component nombre = state.getBlock().getName();
            String id = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();

            player.sendOverlayMessage(
                    Component.literal("§5").append(nombre)
                            .append(Component.literal(" §7[" + id + "]"))
                            .append(Component.literal(" §8" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()
                            ))
            );
        }

        return InteractionResult.SUCCESS;
    }
}

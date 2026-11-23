package com.example.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

/**
 * Lección 3.3: interacción con bloques usando useOn(UseOnContext).
 *
 * A diferencia de use() (clic derecho al aire), useOn() se dispara cuando el jugador
 * hace clic derecho apuntando a un bloque. UseOnContext te da toda la info del clic:
 * nivel, posición clicada, cara, jugador, mano, item, etc.
 */
public class MossWandItem extends Item {

	public MossWandItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		Player player = context.getPlayer();
		ItemStack stack = context.getItemInHand();

		// Ejemplo principal: "enmusgar" algunos bloques al hacer clic derecho.
		// Variaciones que podrías hacer con la misma estructura:
		// - Transformar otros bloques: stone_bricks->mossy_stone_bricks, dirt->grass_block, etc.
		// - Encender/colocar: si es NETHERRACK, colocar fuego encima con Blocks.FIRE.
		// - Inspeccionar: no cambiar bloque, solo mostrar state.getBlock().getName() al jugador.
		// - Gastar el ítem: stack.shrink(1);
		// - Durabilidad: stack.hurtAndBreak(1, player, context.getHand());
		// - Cooldown: player.getCooldowns().addCooldown(stack, 20);
		// - Solo permitir en ciertos bloques: si no es el esperado, return InteractionResult.PASS;
		BlockState newState = getMossyVariant(state);
		if (newState == null) {
			// PASS deja que otros comportamientos (colocar bloque, abrir inventario, etc.) sigan.
			return InteractionResult.PASS;
		}

		// Los cambios de mundo se hacen en servidor.
		if (!level.isClientSide()) {
			level.setBlock(pos, newState, Block.UPDATE_ALL);
			level.playSound(null, pos, SoundEvents.MOSS_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);

			if (player != null) {
				player.sendOverlayMessage(Component.literal("§aBloque enmusgado"));
				// Variación: aplicar cooldown para evitar spam
				// player.getCooldowns().addCooldown(stack, 10);
			}

			// Variación: consumir durabilidad si el ítem la tuviera
			// if (player != null) {
			//     stack.hurtAndBreak(1, player, context.getHand());
			// }
		}

		// SUCCESS indica que manejamos la interacción. En cliente también ayuda a la animación de mano.
		return InteractionResult.SUCCESS;
	}

	/**
	 * Devuelve la variante "musgosa" del bloque, o null si no hay conversión.
	 * Mantener esta lógica separada hace fácil añadir más transformaciones.
	 */
	private static BlockState getMossyVariant(BlockState state) {
		Block block = state.getBlock();

		// Conversiones simples 1:1. Para bloques con propiedades (slabs/stairs/walls),
		// habría que copiar propiedades del state original; aquí empezamos simple.
		if (block == Blocks.COBBLESTONE) return Blocks.MOSSY_COBBLESTONE.defaultBlockState();
		if (block == Blocks.STONE_BRICKS) return Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
		if (block == Blocks.COBBLESTONE_WALL) return Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState();
		if (block == Blocks.STONE_BRICK_WALL) return Blocks.MOSSY_STONE_BRICK_WALL.defaultBlockState();

		// Variación fácil de añadir:
		// if (block == Blocks.COBBLESTONE_STAIRS) return Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState().setValue(...);

		return null;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
			Consumer<Component> tooltipAdder, TooltipFlag flag) {
		tooltipAdder.accept(Component.literal("§7Clic derecho en un bloque: lo enmusga"));
		tooltipAdder.accept(Component.literal("§8Funciona con adoquín y ladrillos de piedra"));
		tooltipAdder.accept(Component.literal("§8Lección 3.3 - useOn"));
		super.appendHoverText(stack, context, display, tooltipAdder, flag);
	}
}

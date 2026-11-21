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

/**
 * Lección 3: ítem con comportamiento personalizado.
 *
 * Para dar comportamiento propio a un ítem, creamos una clase que extiende Item
 * y sobrescribimos los métodos que nos interesan (use, useOn, appendHoverText, etc.).
 */
public class MagicWandItem extends Item {

	public MagicWandItem(Properties properties) {
		super(properties);
	}

	/**
	 * Se llama cuando el jugador hace clic derecho con el ítem en la mano "al aire"
	 * (sin apuntar a un bloque que consuma la interacción).
	 *
	 * Devuelve un InteractionResult para indicar qué pasó:
	 * - SUCCESS / SUCCESS_SERVER / CONSUME: la interacción se consumió.
	 * - FAIL: falló.
	 * - PASS: no hicimos nada, deja que otro sistema lo intente.
	 */
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		// La lógica que modifica el mundo/entidades debe ejecutarse en el servidor.
		// El cliente se encarga sobre todo de animaciones; el servidor es la autoridad.
		if (!level.isClientSide()) {
			// Aplica Velocidad I durante 5 segundos (20 ticks = 1 segundo)
			player.addEffect(new MobEffectInstance(MobEffects.SPEED, 5 * 20, 0));

			// Mensaje visible para el jugador que usó el ítem (overlay/action bar)
			player.sendOverlayMessage(Component.literal("§a¡La varita te impulsa!"));
		}

		// SUCCESS indica que la acción fue manejada (en cliente reproduce animación de mano).
		return InteractionResult.SUCCESS;
	}

	/**
	 * Añade líneas extra al tooltip (la descripción que aparece al pasar el ratón).
	 */
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
			Consumer<Component> tooltipAdder, TooltipFlag flag) {
		tooltipAdder.accept(Component.literal("§7Clic derecho: te da Velocidad I por 5s"));
		tooltipAdder.accept(Component.literal("§8Lección 3 - ítem personalizado"));

		// Llama al super por si la clase base o componentes quieren añadir tooltip.
		super.appendHoverText(stack, context, display, tooltipAdder, flag);
	}
}

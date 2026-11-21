package com.example.item;

import com.example.ExampleMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/**
 * Contiene las "claves" (IDs registrados) de nuestros items.
 * Una ResourceKey identifica un objeto dentro de un registro (aquí, el registro de items)
 * usando el namespace del mod (modid) y el nombre del item.
 */
public class ModItemIds {
	public static final ResourceKey<Item> RUBY = create("ruby"); //IA (item comun)
	public static final ResourceKey<Item> LOPS = create("lops");  //Mones (item comun)
	public static final ResourceKey<Item> POISON_APPLE = create("poison_apple"); //IA (item consumible/comida)
	public static final ResourceKey<Item> CUPCAKE = create("cupcake"); //Mones (item consumible/comida)
	public static final ResourceKey<Item> MAGIC_WAND = create("magic_wand");  //IA (item con comportamineto personalizado)
    public static final ResourceKey<Item> HOLY_HAND = create("holy_hand"); //Mones (item con comportamiento personalizado)

	public static ResourceKey<Item> create(String name) {
		// Crea la clave del item: modid:<name> dentro del registro de items
		return ResourceKey.create(Registries.ITEM, ExampleMod.id(name));
	}

	private ModItemIds() {
	}
}

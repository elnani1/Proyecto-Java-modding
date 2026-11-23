package com.example.item;

import com.example.ExampleMod;
import com.example.item.custom.MagicWandItem;
import com.example.item.custom.HolyHandItem;
import com.example.item.custom.MossWandItem;
import com.example.item.custom.RastreadorItem;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;
import net.minecraft.sounds.SoundEvents;

import java.util.function.Function;

/**
 * Registra y expone las instancias de nuestros items y la pestaña creativa del mod.
 */
public class ModItems {
	// 1) Items (se registran al cargar la clase)
	public static final Item RUBY = register(
			ModItemIds.RUBY,
			Item::new,
			new Item.Properties()
	);

	public static final Item LOPS = register(
			ModItemIds.LOPS,
			Item::new,
			new Item.Properties()
	);

    public static final Item HOLY_HAND = register(
            ModItemIds.HOLY_HAND,
            HolyHandItem::new,
            new Item.Properties().stacksTo(1)
    );

    public static final FoodProperties CUPCAKE_FOOD = new FoodProperties.Builder()
            .nutrition(5)
            .saturationModifier(0.5f)
            .build();

    public static final Consumable CUPCAKE_CONSUMABLE = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                    new MobEffectInstance(MobEffects.HEALTH_BOOST,  3 * 20, 1),
                    1.0f))
            .build();

    public static final Item CUPCAKE = register(
            ModItemIds.CUPCAKE,
            Item::new,
            new Item.Properties().food(CUPCAKE_FOOD, CUPCAKE_CONSUMABLE)
    );

	// --- Lección 3: ítem con comportamiento personalizado ---
	// Usamos nuestra clase MagicWandItem en vez de Item::new, porque queremos
	// sobrescribir use()/appendHoverText() para darle comportamiento propio.
	public static final Item MAGIC_WAND = register(
			ModItemIds.MAGIC_WAND,
			MagicWandItem::new,
			new Item.Properties().stacksTo(1)
	);

	// --- Lección 3.3: interacción con bloques (useOn) ---
	// Aquí el comportamiento depende del bloque clicado. La lógica va en MossWandItem.useOn(UseOnContext).
	// Variaciones: transformar bloques, encender fuego, colocar algo, inspeccionar, gastar durabilidad, cooldown.
	public static final Item MOSS_WAND = register(
			ModItemIds.MOSS_WAND,
			MossWandItem::new,
			new Item.Properties().stacksTo(1)
	);

    public static final Item RASTREADOR = register(
            ModItemIds.RASTREADOR,
            RastreadorItem::new,
            new Item.Properties()
    );

	// --- Lección 2.4: consumibles con distintos ConsumeEffect ---
	// Objetivo principal: aprender a LIMPIAR efectos al consumir.
	//
	// ConsumeEffect es lo que ocurre "al terminar de consumir" un Consumable.
	// Ya conoces ApplyStatusEffectsConsumeEffect (aplicar efectos). En 26.2 también existen:
	// - ClearAllStatusEffectsConsumeEffect: quita TODOS los efectos.
	// - RemoveStatusEffectsConsumeEffect: quita solo ciertos efectos (por Holder o HolderSet).
	// - PlaySoundConsumeEffect: reproduce un sonido.
	// - TeleportRandomlyConsumeEffect: teletransporte aleatorio tipo fruta chorus (diametro).
	//
	// Variaciones (misma idea, distinto uso):
	// - "Leche/mejor leche": solo limpiar -> ClearAllStatusEffectsConsumeEffect.INSTANCE
	// - "Antídoto específico": solo quitar veneno -> new RemoveStatusEffectsConsumeEffect(MobEffects.POISON)
	// - "Tónico ritual": limpiar + sonido -> .onConsume(ClearAll... ).onConsume(new PlaySoundConsumeEffect(...))
	// - "Fruta extraña": teletransporte -> new TeleportRandomlyConsumeEffect() o con diámetro, ej. 16.0f
	// - "Bebida de purga parcial": quitar varios -> new RemoveStatusEffectsConsumeEffect(HolderSet.direct(MobEffects.POISON, MobEffects.WITHER))
	//
	// Para este ejemplo principal implementamos una "infusión purificadora":
	// limpia TODOS los efectos, y además reproduce un sonido al consumir.
	public static final FoodProperties CLEANSING_TEA_FOOD = new FoodProperties.Builder()
			.nutrition(2)
			.saturationModifier(0.2f)
			.alwaysEdible() // tiene sentido poder beberla aunque estés lleno, para curarte efectos
			.build();

	public static final Consumable CLEANSING_TEA_CONSUMABLE = Consumables.defaultDrink() // animación/sonido base de bebida
			.onConsume(ClearAllStatusEffectsConsumeEffect.INSTANCE) // VARIACIÓN principal: limpia TODOS los efectos
			.onConsume(new PlaySoundConsumeEffect(Holder.direct(SoundEvents.ZOMBIE_VILLAGER_CURE))) // VARIACIÓN: feedback sonoro al consumir
			// Otras variaciones rápidas (descomenta/usa como guía):
			// .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.POISON)) // solo quita Veneno
			// .onConsume(new TeleportRandomlyConsumeEffect(16.0f)) // teletransporte tipo chorus, diámetro 16
			.build();

	public static final Item CLEANSING_TEA = register(
			ModItemIds.CLEANSING_TEA,
			Item::new,
			new Item.Properties().food(CLEANSING_TEA_FOOD, CLEANSING_TEA_CONSUMABLE)
	);

	// --- Lección 2.5: resto / transformación al consumir ---
	// Objetivo principal: aprender a que un consumible DEJE UN RESTO o SE TRANSFORME al usarse.
	//
	// La clave es Item.Properties.usingConvertsTo(Item):
	// - Al terminar de consumir, el ítem se reemplaza por el ítem indicado (si hay espacio/stack).
	// - Ejemplos vanilla: sopa de hongos -> bowl, leche -> bucket, botella de miel -> glass bottle.
	//
	// Diferencia importante:
	// - usingConvertsTo(...)  -> resto al CONSUMIR (comer/beber).
	// - craftRemainder(...)   -> resto al CRAFTEAR (no lo cubrimos aquí, pero es la idea hermana).
	//
	// Variaciones (misma idea, distinto uso):
	// - Sopa/estofado: defaultFood + usingConvertsTo(Items.BOWL)
	// - Bebida en botella: defaultDrink + usingConvertsTo(Items.GLASS_BOTTLE)
	// - Bebida en cubo: defaultDrink + usingConvertsTo(Items.BUCKET)
	// - Ítem propio como resto: usingConvertsTo(ModItems.RUBY) o un ítem "frasco_vacio" propio
	// - Sin resto: no pongas usingConvertsTo (como poison_apple/cupcake)
	//
	// Ejemplo principal: "Leche con miel" en botella.
	// - Se bebe (defaultDrink)
	// - Restaura un poco y limpia efectos negativos leves (aquí: quita Veneno; variación: ClearAll)
	// - Al terminar deja Items.GLASS_BOTTLE
	public static final FoodProperties HONEY_MILK_FOOD = new FoodProperties.Builder()
			.nutrition(3)
			.saturationModifier(0.4f)
			.alwaysEdible() // útil como bebida "curativa"
			.build();

	public static final Consumable HONEY_MILK_CONSUMABLE = Consumables.defaultDrink()
			.onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.POISON)) // principal de 2.4 aplicado con criterio
			// Variaciones:
			// .onConsume(ClearAllStatusEffectsConsumeEffect.INSTANCE) // más fuerte, tipo leche
			// .onConsume(new PlaySoundConsumeEffect(Holder.direct(SoundEvents.HONEY_DRINK)))
			.build();

	public static final Item HONEY_MILK = register(
			ModItemIds.HONEY_MILK,
			Item::new,
			new Item.Properties()
					.food(HONEY_MILK_FOOD, HONEY_MILK_CONSUMABLE)
					.usingConvertsTo(net.minecraft.world.item.Items.GLASS_BOTTLE) // CLAVE 2.5: deja botella al beber
					.stacksTo(16) // opcional: como botellas/pociones, apila menos
	);

	// --- Lección 2: item de comida ---
	// FoodProperties: define la nutrición/saturación y si se puede comer siempre.
	public static final FoodProperties POISON_APPLE_FOOD = new FoodProperties.Builder()
			.nutrition(4)           // puntos de hambre que restaura (1 muslito = 2 puntos)
			.saturationModifier(0.3f) // modificador de saturación (cuánto "dura" la comida)
			.alwaysEdible()         // permite comer aunque la barra de hambre esté llena
			.build();

	// Consumable: define cómo se consume (animación/sonido por defecto) y efectos al terminar.
	// La duración de los efectos va en ticks: 20 ticks = 1 segundo.
	public static final Consumable POISON_APPLE_CONSUMABLE = Consumables.defaultFood()
			.onConsume(new ApplyStatusEffectsConsumeEffect(
					new MobEffectInstance(MobEffects.POISON, 6 * 20, 1), // Veneno II por 6 s
					1.0f)) // probabilidad de aplicar el efecto (1.0 = 100%)
			.build();

	// Item comestible: se asocian FoodProperties y Consumable mediante Item.Properties.
	public static final Item POISON_APPLE = register(
			ModItemIds.POISON_APPLE,
			Item::new,
			new Item.Properties().food(POISON_APPLE_FOOD, POISON_APPLE_CONSUMABLE)
	);

	// 2) Pestaña creativa propia del mod
	//    - Clave (ID) de la pestaña dentro del registro de pestañas creativas
	public static final ResourceKey<CreativeModeTab> MOD_TAB_KEY = ResourceKey.create(
			Registries.CREATIVE_MODE_TAB, ExampleMod.id("mod_tab"));

	//    - Definición de la pestaña: icono, título (clave de traducción) y contenido
	public static final CreativeModeTab MOD_TAB = FabricCreativeModeTab.builder()
			.icon(() -> new ItemStack(ModItems.RUBY))
			.title(Component.translatable("itemGroup.modid.mod_tab"))
			.displayItems((params, output) -> {
				// Aquí se listan todos los items que aparecerán en la pestaña del mod
				output.accept(ModItems.RUBY);
				output.accept(ModItems.LOPS);
				output.accept(ModItems.POISON_APPLE);
                output.accept(ModItems.CUPCAKE);
				output.accept(ModItems.CLEANSING_TEA);
				output.accept(ModItems.HONEY_MILK);
				output.accept(ModItems.MAGIC_WAND);
				output.accept(ModItems.MOSS_WAND);
                output.accept(ModItems.HOLY_HAND);
                output.accept(ModItems.RASTREADOR);
			})
			.build();

	/**
	 * Registra un item en el registro de items de Minecraft.
	 * 1) Asocia las propiedades con su ID (setId) — obligatorio en versiones modernas.
	 * 2) Crea la instancia usando la fábrica (por ej. Item::new).
	 * 3) Lo registra en BuiltInRegistries.ITEM con su clave.
	 */
	public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
		Item item = itemFactory.apply(settings.setId(itemKey));
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);
		return item;
	}

	/**
	 * "Inicializa" la clase: al llamarla desde el ModInitializer forzamos que se
	 * ejecuten los campos static de arriba (y por tanto se registren los items y se
	 * construya la pestaña). Aquí registramos la pestaña creativa en el juego.
	 */
	public static void initialize() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MOD_TAB_KEY, MOD_TAB);
	}

	private ModItems() {
	}
}

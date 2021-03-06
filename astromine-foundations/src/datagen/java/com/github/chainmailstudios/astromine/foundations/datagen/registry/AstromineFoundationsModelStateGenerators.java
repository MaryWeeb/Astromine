package com.github.chainmailstudios.astromine.foundations.datagen.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.ModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.GenericBlockModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.GenericItemModelGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.HandheldItemModelGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.SimpleBlockItemModelGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.ColumnBlockSetModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.GenericBlockSetModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.GenericItemSetModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.HandheldItemSetModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsItems;

public class AstromineFoundationsModelStateGenerators extends AstromineModelStateGenerators {
	public final ModelStateGenerator INGOT = register(new GenericItemSetModelStateGenerator(MaterialItemType.INGOT));
	public final ModelStateGenerator GEM = register(new GenericItemSetModelStateGenerator(MaterialItemType.GEM));
	public final ModelStateGenerator MISC_RESOURCE = register(new GenericItemSetModelStateGenerator(MaterialItemType.MISC_RESOURCE));

	public final ModelStateGenerator NUGGET = register(new GenericItemSetModelStateGenerator(MaterialItemType.NUGGET));
	public final ModelStateGenerator FRAGMENT = register(new GenericItemSetModelStateGenerator(MaterialItemType.FRAGMENT));

	public final ModelStateGenerator BLOCK = register(new GenericBlockSetModelStateGenerator(MaterialItemType.BLOCK));
	public final ModelStateGenerator ORE = register(new GenericBlockSetModelStateGenerator(MaterialItemType.ORE));

	public final ModelStateGenerator METEOR_ORE = register(new ColumnBlockSetModelStateGenerator(MaterialItemType.METEOR_ORE, AstromineCommon.identifier("block/meteor_stone")));

	public final ModelStateGenerator METEOR_CLUSTER = register(new GenericItemSetModelStateGenerator(MaterialItemType.METEOR_CLUSTER));

	public final ModelStateGenerator DUST = register(new GenericItemSetModelStateGenerator(MaterialItemType.DUST));
	public final ModelStateGenerator TINY_DUST = register(new GenericItemSetModelStateGenerator(MaterialItemType.TINY_DUST));

	public final ModelStateGenerator GEAR = register(new GenericItemSetModelStateGenerator(MaterialItemType.GEAR));
	public final ModelStateGenerator PLATES = register(new GenericItemSetModelStateGenerator(MaterialItemType.PLATES));
	public final ModelStateGenerator WIRE = register(new GenericItemSetModelStateGenerator(MaterialItemType.WIRE));

	public final ModelStateGenerator PICKAXE = register(new HandheldItemSetModelStateGenerator(MaterialItemType.PICKAXE));
	public final ModelStateGenerator AXE = register(new HandheldItemSetModelStateGenerator(MaterialItemType.AXE));
	public final ModelStateGenerator SHOVEL = register(new HandheldItemSetModelStateGenerator(MaterialItemType.SHOVEL));
	public final ModelStateGenerator SWORD = register(new HandheldItemSetModelStateGenerator(MaterialItemType.SWORD));
	public final ModelStateGenerator HOE = register(new HandheldItemSetModelStateGenerator(MaterialItemType.HOE));

	public final ModelStateGenerator MATTOCK = register(new HandheldItemSetModelStateGenerator(MaterialItemType.MATTOCK));
	public final ModelStateGenerator MINING_TOOL = register(new HandheldItemSetModelStateGenerator(MaterialItemType.MINING_TOOL));

	public final ModelStateGenerator HAMMER = register(new HandheldItemSetModelStateGenerator(MaterialItemType.HAMMER));
	public final ModelStateGenerator EXCAVATOR = register(new HandheldItemSetModelStateGenerator(MaterialItemType.EXCAVATOR));

	public final ModelStateGenerator HELMET = register(new GenericItemSetModelStateGenerator(MaterialItemType.HELMET));
	public final ModelStateGenerator CHESTPLATE = register(new GenericItemSetModelStateGenerator(MaterialItemType.CHESTPLATE));
	public final ModelStateGenerator LEGGINGS = register(new GenericItemSetModelStateGenerator(MaterialItemType.LEGGINGS));
	public final ModelStateGenerator BOOTS = register(new GenericItemSetModelStateGenerator(MaterialItemType.BOOTS));

	public final ModelStateGenerator WRENCH = register(new HandheldItemModelGenerator(
			AstromineFoundationsItems.COPPER_WRENCH,
			AstromineFoundationsItems.BRONZE_WRENCH,
			AstromineFoundationsItems.STEEL_WRENCH
	));

	public final ModelStateGenerator STANDARD_BLOCKS = register(new GenericBlockModelStateGenerator(
			AstromineFoundationsBlocks.METEOR_STONE
	));

	public final ModelStateGenerator CUSTOM_MODEL_AND_STATE_BLOCKS = register(new SimpleBlockItemModelGenerator(
			AstromineFoundationsBlocks.METEOR_STONE_SLAB,
			AstromineFoundationsBlocks.METEOR_STONE_STAIRS
	));
}

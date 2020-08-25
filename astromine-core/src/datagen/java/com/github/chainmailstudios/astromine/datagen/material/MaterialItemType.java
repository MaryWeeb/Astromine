package com.github.chainmailstudios.astromine.datagen.material;

public enum MaterialItemType {
	INGOT,
	GEM(""),
	MISC_RESOURCE(""),
	NUGGET,
	FRAGMENT,
	BLOCK,
	ORE,
	METEOR_ORE("meteor", "ore"),
	ASTEROID_ORE("asteroid", "ore"),
	METEOR_CLUSTER("meteor", "cluster"),
	ASTEROID_CLUSTER("asteroid", "cluster"),
	DUST,
	TINY_DUST,
	GEAR,
	PLATES,
	WIRE,
	PICKAXE,
	AXE,
	SHOVEL,
	SWORD,
	HOE,
	MATTOCK,
	MINING_TOOL,
	HAMMER,
	EXCAVATOR,
	HELMET,
	CHESTPLATE,
	LEGGINGS,
	BOOTS;

	final String prefix;
	final String suffix;

	MaterialItemType() {
		this.prefix = "";
		this.suffix = this.getName();
	}

	MaterialItemType(String suffix) {
		this("", suffix);
	}

	MaterialItemType(String prefix, String suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public String getItemId(String materialName) {
		return prefix + (prefix.isEmpty() ? "" : "_") + materialName + (suffix.isEmpty() ? "" : "_") + suffix;
	}

	public String getName() {
		return this.name().toLowerCase();
	}

	public boolean isBlock() {
		return this == BLOCK || this == ORE || this == METEOR_ORE || this == ASTEROID_ORE;
	}
}

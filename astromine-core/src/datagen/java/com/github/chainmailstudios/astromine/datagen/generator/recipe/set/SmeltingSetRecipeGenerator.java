package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.CookingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

public class SmeltingSetRecipeGenerator extends CookingSetRecipeGenerator {
	public SmeltingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int time, float experience) {
		super(input, output, time, experience);
	}

	public SmeltingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, float experience) {
		this(input, output, 200, experience);
	}

	public SmeltingSetRecipeGenerator(MaterialItemType input, MaterialItemType output) {
		this(input, output, 200, 0.1f);
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		CookingRecipeJsonFactory
			.createSmelting(
				set.getIngredient(input),
				set.getItem(output),
				experience,
				time)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.offerTo(recipes, getRecipeId(set));
	}

	@Override
	public String getRecipeName(MaterialSet set) {
		return set.getItemIdPath(output) + "_from_smelting_" + input.getName();
	}
}

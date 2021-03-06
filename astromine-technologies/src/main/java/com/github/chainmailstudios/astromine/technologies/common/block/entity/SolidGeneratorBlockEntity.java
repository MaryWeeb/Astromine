/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.technologies.common.block.entity;

import net.fabricmc.fabric.api.registry.FuelRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

import com.github.chainmailstudios.astromine.common.block.base.BlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyInventoryBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.recipe.SolidGeneratingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.technologies.common.block.SolidGeneratorBlock;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class SolidGeneratorBlockEntity extends ComponentEnergyInventoryBlockEntity implements RecipeConsumer, Tickable {
	public double current = 0;
	public int limit = 100;

	public boolean isActive = false;

	public boolean[] activity = { false, false, false, false, false };

	private Optional<SolidGeneratingRecipe> recipe = Optional.empty();

	public SolidGeneratorBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(1).withListener((inv) -> {
			if (hasWorld() && !this.world.isClient() && (!recipe.isPresent() || !recipe.get().canCraft(this)))
				recipe = (Optional) world.getRecipeManager().getAllOfType(SolidGeneratingRecipe.Type.INSTANCE).values().stream().filter(recipe -> recipe instanceof SolidGeneratingRecipe).filter(recipe -> ((SolidGeneratingRecipe) recipe).canCraft(this)).findFirst();
		});
	}

	@Override
	public double getCurrent() {
		return current;
	}

	@Override
	public void setCurrent(double current) {
		this.current = current;
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public void increment() {
		current += 1 * ((SolidGeneratorBlock) this.getCachedState().getBlock()).getMachineSpeed();
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		readRecipeProgress(tag);
		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		writeRecipeProgress(tag);
		return super.toTag(tag);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient())
			return;

		if (recipe.isPresent()) {
			recipe.get().tick(this);

			if (recipe.isPresent() && !recipe.get().canCraft(this)) {
				recipe = Optional.empty();
			}

			isActive = true;
		} else {
			ItemStack burnStack = itemComponent.getStack(0);

			Integer value = FuelRegistry.INSTANCE.get(burnStack.getItem());

			boolean isFuel = !(burnStack.getItem() instanceof BucketItem) && value != null && value > 0;

			if (isFuel) {
				if (current == 0) {
					limit = value / 2;
					current++;
					burnStack.decrement(1);
				}
			}

			double produced = 5;
			for (int i = 0; i < 3 * ((SolidGeneratorBlock) this.getCachedState().getBlock()).getMachineSpeed(); i++) {
				if (current > 0 && current <= limit) {
					if (EnergyUtilities.hasAvailable(asEnergy(), produced)) {
						current++;
						asEnergy().insert(produced);
					}
				} else {
					current = 0;
					limit = 100;
					break;
				}
			}

			isActive = isFuel || current != 0;
		}

		if (activity.length - 1 >= 0)
			System.arraycopy(activity, 1, activity, 0, activity.length - 1);

		activity[4] = isActive;

		if (isActive && !activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, true));
		} else if (!isActive && activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, false));
		}
	}

	public static class Primitive extends SolidGeneratorBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlocks.PRIMITIVE_SOLID_GENERATOR, AstromineTechnologiesBlockEntityTypes.PRIMITIVE_SOLID_GENERATOR);
		}

	}

	public static class Basic extends SolidGeneratorBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlocks.BASIC_SOLID_GENERATOR, AstromineTechnologiesBlockEntityTypes.BASIC_SOLID_GENERATOR);
		}

	}

	public static class Advanced extends SolidGeneratorBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlocks.ADVANCED_SOLID_GENERATOR, AstromineTechnologiesBlockEntityTypes.ADVANCED_SOLID_GENERATOR);
		}

	}

	public static class Elite extends SolidGeneratorBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlocks.ELITE_SOLID_GENERATOR, AstromineTechnologiesBlockEntityTypes.ELITE_SOLID_GENERATOR);
		}

	}
}

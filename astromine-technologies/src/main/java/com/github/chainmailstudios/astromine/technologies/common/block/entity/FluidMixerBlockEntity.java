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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

import com.github.chainmailstudios.astromine.common.block.base.BlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.recipe.FluidMixingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.FluidMixerBlock;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class FluidMixerBlockEntity extends ComponentEnergyFluidBlockEntity implements RecipeConsumer, Tickable {
	private static final int FIRST_INPUT_FLUID_VOLUME = 0;
	private static final int SECOND_INPUT_FLUID_VOLUME = 1;
	private static final int OUTPUT_FLUID_VOLUME = 2;
	public double current = 0;
	public int limit = 100;
	public boolean isActive = false;
	public boolean[] activity = { false, false, false, false, false };
	private Optional<FluidMixingRecipe> recipe = Optional.empty();

	public FluidMixerBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);

		fluidComponent.getVolume(FIRST_INPUT_FLUID_VOLUME).setSize(new Fraction(4, 1));
		fluidComponent.getVolume(SECOND_INPUT_FLUID_VOLUME).setSize(new Fraction(4, 1));
		fluidComponent.getVolume(OUTPUT_FLUID_VOLUME).setSize(new Fraction(4, 1));

		addEnergyListener(fluidComponent::dispatchConsumers);
	}

	abstract Fraction getTankSize();

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		return new SimpleFluidInventoryComponent(3).withListener((inv) -> {
			if (this.world != null && !this.world.isClient() && (!recipe.isPresent() || !recipe.get().canCraft(this)))
				recipe = (Optional) world.getRecipeManager().getAllOfType(FluidMixingRecipe.Type.INSTANCE).values().stream().filter(recipe -> recipe instanceof FluidMixingRecipe).filter(recipe -> ((FluidMixingRecipe) recipe).canCraft(this)).findFirst();
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
		current += 1 * ((FluidMixerBlock) this.getCachedState().getBlock()).getMachineSpeed();
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
			isActive = false;
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

	public static class Primitive extends FluidMixerBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlocks.PRIMITIVE_FLUID_MIXER, AstromineTechnologiesBlockEntityTypes.PRIMITIVE_FLUID_MIXER);
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().primitiveFluidMixerFluid, 1);
		}
	}

	public static class Basic extends FluidMixerBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlocks.BASIC_FLUID_MIXER, AstromineTechnologiesBlockEntityTypes.BASIC_FLUID_MIXER);
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().basicFluidMixerFluid, 1);
		}
	}

	public static class Advanced extends FluidMixerBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER, AstromineTechnologiesBlockEntityTypes.ADVANCED_FLUID_MIXER);
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().advancedFluidMixerFluid, 1);
		}
	}

	public static class Elite extends FluidMixerBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlocks.ELITE_FLUID_MIXER, AstromineTechnologiesBlockEntityTypes.ELITE_FLUID_MIXER);
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().eliteFluidMixerFluid, 1);
		}
	}
}

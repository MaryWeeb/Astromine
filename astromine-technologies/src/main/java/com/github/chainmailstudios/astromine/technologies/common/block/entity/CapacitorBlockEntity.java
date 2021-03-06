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
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tickable;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyInventoryBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

public abstract class CapacitorBlockEntity extends ComponentEnergyInventoryBlockEntity implements Tickable {
	public CapacitorBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(2);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient())
			return;
		// input
		ItemStack inputStack = itemComponent.getStack(0);
		if (Energy.valid(inputStack)) {
			EnergyHandler energyHandler = Energy.of(inputStack);
			energyHandler.into(asEnergy()).move(2048);
		}

		ItemStack outputStack = itemComponent.getStack(1);
		if (Energy.valid(outputStack)) {
			EnergyHandler energyHandler = Energy.of(outputStack);
			asEnergy().into(energyHandler).move(2048);
		}
	}

	public static class Primitive extends CapacitorBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlocks.PRIMITIVE_CAPACITOR, AstromineTechnologiesBlockEntityTypes.PRIMITIVE_CAPACITOR);
		}

	}

	public static class Basic extends CapacitorBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlocks.BASIC_CAPACITOR, AstromineTechnologiesBlockEntityTypes.BASIC_CAPACITOR);
		}

	}

	public static class Advanced extends CapacitorBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlocks.ADVANCED_CAPACITOR, AstromineTechnologiesBlockEntityTypes.ADVANCED_CAPACITOR);
		}

	}

	public static class Elite extends CapacitorBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlocks.ELITE_CAPACITOR, AstromineTechnologiesBlockEntityTypes.ELITE_CAPACITOR);
		}

	}
}

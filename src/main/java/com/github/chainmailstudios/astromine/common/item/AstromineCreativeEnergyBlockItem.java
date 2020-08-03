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

package com.github.chainmailstudios.astromine.common.item;

import com.github.chainmailstudios.astromine.common.block.base.EnergyBlock;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import team.reborn.energy.*;

import java.util.List;

public class AstromineCreativeEnergyBlockItem extends AstromineBlockItem implements EnergyStorage {
	private final EnergyBlock energyBlock;

	AstromineCreativeEnergyBlockItem(Block block, Settings settings) {
		super(block, settings);
		this.energyBlock = (EnergyBlock) block;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		EnergyHandler energyHandler = Energy.of(stack);
		tooltip.add(EnergyUtilities.compoundDisplayColored(energyHandler.getEnergy(), energyHandler.getMaxStored()));

		super.appendTooltip(stack, world, tooltip, context);
	}

	@Override
	public double getMaxStoredPower() {
		return energyBlock.getEnergyCapacity();
	}

	@Override
	public EnergyTier getTier() {
		return EnergyTier.INFINITE;
	}

	@Override
	public double getStored(EnergySide face) {
		return getMaxStoredPower();
	}

	@Override
	public void setStored(double amount) {

	}
}

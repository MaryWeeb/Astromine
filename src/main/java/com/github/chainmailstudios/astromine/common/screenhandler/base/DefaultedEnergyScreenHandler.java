package com.github.chainmailstudios.astromine.common.screenhandler.base;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;

public class DefaultedEnergyScreenHandler extends DefaultedBlockEntityScreenHandler {
	public DefaultedEnergyBlockEntity blockEntity;

	public DefaultedEnergyScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);

		blockEntity = (DefaultedEnergyBlockEntity) world.getBlockEntity(position);
	}
}
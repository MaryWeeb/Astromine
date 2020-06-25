package com.github.chainmailstudios.astromine.common.block.entity.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public abstract class DefaultedFluidBlockEntity extends DefaultedBlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	protected final SimpleFluidInventoryComponent fluidComponent = new SimpleFluidInventoryComponent(1);

	public DefaultedFluidBlockEntity(BlockEntityType<?> type) {
		super(type);

		addComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, fluidComponent);
	}
}

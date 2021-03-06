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

package com.github.chainmailstudios.astromine.discoveries.common.entity;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.entity.base.ComponentFluidEntity;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesCriteria;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesParticles;
import com.github.chainmailstudios.astromine.discoveries.common.screenhandler.RocketScreenHandler;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsItems;
import com.zundrel.wrenchable.entity.EntityWrenchable;
import com.zundrel.wrenchable.wrench.Wrench;
import io.netty.buffer.Unpooled;

import javax.annotation.Nullable;
import java.util.Optional;

public class RocketEntity extends ComponentFluidEntity implements ExtendedScreenHandlerFactory, EntityWrenchable {
	public static final Identifier ROCKET_SPAWN = AstromineCommon.identifier("rocket_spawn");
	private static final TrackedData<Boolean> IS_GO = DataTracker.registerData(RocketEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public RocketEntity(EntityType<?> type, World world) {
		super(type, world);

		this.getDataTracker().set(IS_GO, false);
	}

	@Override
	public FluidInventoryComponent createFluidComponent() {
		FluidInventoryComponent fluidComponent = new SimpleFluidInventoryComponent(1);
		fluidComponent.getVolume(0).setSize(Fraction.ofWhole(128));
		return fluidComponent;
	}

	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(IS_GO, false);
	}

	@Override
	public boolean method_30948() {
		return !this.removed;
	}

	@Override
	public boolean collides() {
		return !this.removed;
	}

	@Override
	public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
		if (player.world.isClient || this.removed) {
			return ActionResult.CONSUME;
		}

		ItemStack stack = player.getStackInHand(hand);

		if (stack.getItem() instanceof Wrench) {
			return ActionResult.PASS;
		}

		if (player.isSneaking()) {
//			player.openHandledScreen(this);
			return ActionResult.SUCCESS;
		}

		if (stack.getItem() == Items.FLINT_AND_STEEL) {
			this.getDataTracker().set(IS_GO, true);
			if (player instanceof ServerPlayerEntity) {
				AstromineDiscoveriesCriteria.LAUNCH_ROCKET.trigger((ServerPlayerEntity) player);
			}
			return ActionResult.SUCCESS;
		}

		player.startRiding(this);

		return super.interactAt(player, hitPos, hand);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());

		packet.writeDouble(this.getX());
		packet.writeDouble(this.getY());
		packet.writeDouble(this.getZ());
		packet.writeUuid(this.getUuid());
		packet.writeInt(this.getEntityId());

		return ServerSidePacketRegistry.INSTANCE.toPacket(ROCKET_SPAWN, packet);
	}

	@Override
	public void updatePassengerPosition(Entity passenger) {
		if (this.hasPassenger(passenger)) {
			Vector3f position = new Vector3f(0, 7.75f, 0);
			passenger.updatePosition(getX() + position.getZ(), getY() + position.getY(), getZ());
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (this.getDataTracker().get(IS_GO)) {
			addVelocity(0, 0.0015f, 0);
			this.move(MovementType.SELF, this.getVelocity());

			Vec3d thrustVec = new Vec3d(0.035, -2.5f, 0.035);
			Vec3d speed = new Vec3d(0.02, -0.2f, 0.02);

			for (int i = 0; i < 90; ++i) {
				speed = speed.rotateY(1);
				spawnParticles(thrustVec, speed);
			}
		} else {
			setVelocity(Vec3d.ZERO);
			this.velocityDirty = true;
		}

		if (!world.isClient) {
			if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
				int j = MathHelper.floor(this.getY());
				int n = MathHelper.floor(this.getX());
				int o = MathHelper.floor(this.getZ());

				boolean bl = false;

				for (int p = -2; p <= 2; ++p) {
					for (int q = -2; q <= 2; ++q) {
						for (int r = 0; r <= 22; ++r) {
							int s = n + p;
							int t = j + r;
							int u = o + q;

							BlockPos blockPos = new BlockPos(s, t, u);
							BlockState blockState = this.world.getBlockState(blockPos);

							float power = 0;

							if (WitherEntity.canDestroy(blockState)) {
								bl = true;
								power = 2.1f;
							}

							if (power > 0) {
								this.world.createExplosion(null, DamageSource.explosion((LivingEntity) null), new ExplosionBehavior() {
									@Override
									public Optional<Float> getBlastResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState) {
										return Optional.empty();
									}

									@Override
									public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
										return RocketEntity.this.canExplosionDestroyBlock(explosion, world, pos, state, power);
									}
								}, blockPos.getX() + .5, blockPos.getY() + .5, blockPos.getZ() + .5, power, false, Explosion.DestructionType.DESTROY);
							}
						}
					}
				}

				if (bl) {
					this.world.syncWorldEvent(null, 1022, this.getBlockPos(), 0);
				}
			}
		}
	}

	public void spawnParticles(Vec3d thrustVec, Vec3d speed) {
		world.addParticle(AstromineDiscoveriesParticles.ROCKET_FLAME, getX() + ((thrustVec.getX() - (Math.min(0.6f, random.nextFloat())) * (random.nextBoolean() ? 1 : -1))), getY() + thrustVec.getY(), getZ() + ((thrustVec.getZ() - (Math.min(0.6f, random.nextFloat())) * (random.nextBoolean()
			? 1 : -1))), speed.getX(), speed.getY(), speed.getZ());
	}

	@Override
	public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf buffer) {
		buffer.writeInt(this.getEntityId());
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new RocketScreenHandler(syncId, player, getEntityId());
	}

	@Override
	public void onWrenched(World world, PlayerEntity player, EntityHitResult result) {
		this.removeAllPassengers();
		this.kill();
	}

	@Override
	public Vec3d updatePassengerForDismount(LivingEntity passenger) {
		return new Vec3d((this.getX()-2)+this.getEntityWorld().getRandom().nextInt(5), this.getY(), (this.getZ()-2)+this.getEntityWorld().getRandom().nextInt(5));
	}
}

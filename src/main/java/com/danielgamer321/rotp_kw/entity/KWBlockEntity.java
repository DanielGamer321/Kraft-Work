package com.danielgamer321.rotp_kw.entity;

import com.danielgamer321.rotp_kw.init.AddonStands;
import com.danielgamer321.rotp_kw.init.InitEntities;
import com.danielgamer321.rotp_kw.power.impl.stand.type.LockedPosition;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class KWBlockEntity extends Entity {
    private boolean timeStop = false;
    private LockedPosition lockedPosition;

    public KWBlockEntity(World world, BlockPos blockPos) {
        this(InitEntities.KW_BLOCK.get(), world);
        this.moveTo(Vector3d.atBottomCenterOf(blockPos));
    }

    public KWBlockEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public void setLockedPosition(@Nullable LivingEntity standUser) {
        this.lockedPosition = new LockedPosition(standUser);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide()) {
            boolean validLockedPos = false;
            LivingEntity user = (LivingEntity) lockedPosition.getUser((ServerWorld) level);
            if (user != null && IStandPower.getStandPowerOptional(user).map(stand ->
                    stand.hasPower() && stand.getType() == AddonStands.KRAFT_WORK.getStandType()).orElse(false)) {
                BlockPos blockPos = blockPosition();
                double distance = this.distanceToSqr(user);
                if (user.isAlive() && distance <= 140 && blockPos != null && !level.isEmptyBlock(blockPos)) {
                    validLockedPos = true;
                    lockedPosition.tick(level, getBoundingBox().inflate(0.1D));
                }
            }
            if (!validLockedPos) {
                remove();
            }
        }
    }

    @Override
    public void canUpdate(boolean canUpdate) {
        this.timeStop = !canUpdate;
        if (canUpdate) {
            super.canUpdate(canUpdate);
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        timeStop = nbt.getBoolean("TimeStop");
        if (nbt.contains("LockedPosition", 10)) {
            this.lockedPosition = LockedPosition.fromNBT(nbt.getCompound("LockedPosition"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.putBoolean("TimeStop", timeStop);
        if (lockedPosition != null) {
            nbt.put("LockedPosition", lockedPosition.toNBT());
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}

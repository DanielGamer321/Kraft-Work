package com.danielgamer321.rotp_kw.capability.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

public class LivingUtilCap {
    private final LivingEntity entity;
    private boolean blockingItemsStatus = false;

    public LivingUtilCap(LivingEntity entity) {
        this.entity = entity;
    }

    public void setBlockingItemsStatus(boolean Status) {
        this.blockingItemsStatus = Status;
    }

    public boolean getBlockingItemsStatus() {
        return blockingItemsStatus;
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("BlockingItemsStatus", blockingItemsStatus);
        return nbt;
    }

    public void fromNBT(CompoundNBT nbt) {
        this.blockingItemsStatus = nbt.getBoolean("BlockingItemsStatus");
    }
}

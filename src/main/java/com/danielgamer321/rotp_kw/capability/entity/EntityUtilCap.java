package com.danielgamer321.rotp_kw.capability.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;

public class EntityUtilCap {
    private final Entity entity;
    private boolean positionLocking;
    
    public EntityUtilCap(Entity entity) {
        this.entity = entity;
    }

    public void setPositionLocking(boolean locked) {
        this.positionLocking = locked;
    }

    public boolean getPositionLocking() {
        return positionLocking;
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("PositionLocking", positionLocking);
        return nbt;
    }

    public void fromNBT(CompoundNBT nbt) {
        this.positionLocking = nbt.getBoolean("PositionLocking");
    }
}

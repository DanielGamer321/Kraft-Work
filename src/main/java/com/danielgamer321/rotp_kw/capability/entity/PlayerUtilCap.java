package com.danielgamer321.rotp_kw.capability.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class PlayerUtilCap {
    private final PlayerEntity player;
    private boolean projectiveLockStatus = false;
    private boolean closedInventory = false;

    public PlayerUtilCap(PlayerEntity player) {
        this.player = player;
    }

    public void setProjectiveLockStatus(boolean Status) {
        this.projectiveLockStatus = Status;
    }

    public boolean getProjectiveLockStatus() {
        return projectiveLockStatus;
    }

    public void inventoryStatus(boolean Status) {
        this.closedInventory = Status;
    }

    public boolean inventoryStatus() {
        return closedInventory;
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("ProjectiveLockStatus", projectiveLockStatus);
        nbt.putBoolean("InventoryStatus", closedInventory);
        return nbt;
    }

    public void fromNBT(CompoundNBT nbt) {
        this.projectiveLockStatus = nbt.getBoolean("ProjectiveLockStatus");
        this.closedInventory = nbt.getBoolean("InventoryStatus");
    }
}

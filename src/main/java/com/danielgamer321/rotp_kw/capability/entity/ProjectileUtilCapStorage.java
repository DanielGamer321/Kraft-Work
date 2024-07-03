package com.danielgamer321.rotp_kw.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ProjectileUtilCapStorage implements IStorage< ProjectileUtilCap> {

    @Override
    public INBT writeNBT(Capability< ProjectileUtilCap> capability,  ProjectileUtilCap instance, Direction side) {
        return instance.toNBT();
    }

    @Override
    public void readNBT(Capability< ProjectileUtilCap> capability,  ProjectileUtilCap instance, Direction side, INBT nbt) {
        instance.fromNBT((CompoundNBT) nbt);
    }
}
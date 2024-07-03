package com.danielgamer321.rotp_kw.capability.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ProjectileUtilCapProvider implements ICapabilitySerializable<INBT>{
    @CapabilityInject(ProjectileUtilCap.class)
    public static Capability<ProjectileUtilCap> CAPABILITY = null;
    private LazyOptional<ProjectileUtilCap> instance;
    
    public ProjectileUtilCapProvider(Entity projectile) {
        this.instance = LazyOptional.of(() -> new ProjectileUtilCap(projectile));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Projectile capability LazyOptional is not attached.")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Projectile capability LazyOptional is not attached.")), null, nbt);
    }
}

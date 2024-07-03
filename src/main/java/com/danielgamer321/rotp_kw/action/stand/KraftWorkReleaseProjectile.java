package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.capability.entity.EntityUtilCapProvider;
import com.danielgamer321.rotp_kw.capability.entity.ProjectileUtilCapProvider;
import com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.world.World;

public class KraftWorkReleaseProjectile extends StandAction {

    public KraftWorkReleaseProjectile(Builder builder) {
        super(builder);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        String lock_id = String.valueOf(user.getUUID());
        world.getEntitiesOfClass(Entity.class, user.getBoundingBox().inflate(1), entity -> entity.getTags().contains(lock_id)).forEach(entity -> {
            boolean PositionLocking = entity.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
            if (entity instanceof ProjectileEntity) {
                ProjectileEntity projectile = (ProjectileEntity) entity;
                boolean ReadyToRelease = projectile.getCapability(ProjectileUtilCapProvider.CAPABILITY).map(cap -> cap.getReadyToRelease()).orElse(false);
                if (PositionLocking && (ReadyToRelease || projectile instanceof FireworkRocketEntity ||
                        projectile instanceof AbstractFireballEntity)) {
                    projectile.getCapability(EntityUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.setPositionLocking(false));
                }
            }
            else if (entity instanceof EyeOfEnderEntity && PositionLocking) {
                KraftWorkStandType.setPositionLockingServerSide(entity, false);
            }
        });
    }

}

package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.capability.entity.EntityUtilCapProvider;
import com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EyeOfEnderEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;

public class KraftWorkReleaseAllProjectiles extends StandAction {

    public KraftWorkReleaseAllProjectiles(Builder builder) {
        super(builder);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        String lock_id = String.valueOf(user.getUUID());
        world.getEntitiesOfClass(Entity.class, user.getBoundingBox().inflate(12),
                entity -> entity.getTags().contains(lock_id)).forEach(entity -> {
            boolean PositionLocking = entity.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
            if ((entity instanceof ProjectileEntity || entity instanceof EyeOfEnderEntity) && PositionLocking) {
                KraftWorkStandType.setPositionLockingServerSide(entity, false);
            }
        });
    }

}

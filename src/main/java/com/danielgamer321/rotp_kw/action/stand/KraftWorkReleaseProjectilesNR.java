package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.capability.entity.EntityUtilCapProvider;
import com.danielgamer321.rotp_kw.capability.entity.ProjectileUtilCapProvider;
import com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;

public class KraftWorkReleaseProjectilesNR extends StandAction {

    public KraftWorkReleaseProjectilesNR(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (!(user instanceof PlayerEntity)) {
            return ActionConditionResult.NEGATIVE;
        }
        return super.checkSpecificConditions(user, power, target);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        String lock_id = String.valueOf(user.getUUID());
        world.getEntitiesOfClass(ProjectileEntity.class, user.getBoundingBox().inflate(13),
                entity -> entity.getTags().contains(lock_id)).forEach(projectile -> {
            boolean PositionLocking = projectile.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
            boolean readyToRelease = projectile.getCapability(ProjectileUtilCapProvider.CAPABILITY).map(cap -> cap.getReadyToRelease()).orElse(false);
            if (PositionLocking && !readyToRelease && !(projectile instanceof EnderPearlEntity ||
                    projectile instanceof FireworkRocketEntity || projectile instanceof AbstractFireballEntity) &&
                    !KraftWorkReleaseBeneficial.BeneficialItems(projectile)) {
                KraftWorkStandType.setPositionLockingServerSide(projectile, false);
            }
        });
    }

}

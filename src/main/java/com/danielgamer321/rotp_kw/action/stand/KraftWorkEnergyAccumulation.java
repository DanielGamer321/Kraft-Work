package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.capability.entity.EntityUtilCapProvider;
import com.danielgamer321.rotp_kw.capability.entity.ProjectileUtilCapProvider;
import com.danielgamer321.rotp_kw.entity.damaging.projectile.KWItemEntity;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.entity.stand.StandPose;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

public class KraftWorkEnergyAccumulation extends StandEntityAction {
    public static final StandPose GIVE_ENERGY_POSE = new StandPose("KW_GIVE_ENERGY", true);
    public KraftWorkEnergyAccumulation(Builder builder) {
        super(builder);
    }

    @Override
    public void standTickPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        world.getEntitiesOfClass(ProjectileEntity.class, standEntity.getBoundingBox().inflate(standEntity.getAttributeValue(ForgeMod.REACH_DISTANCE.get()))).forEach(projectile -> {
            boolean PositionLocking = projectile.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
            if (PositionLocking) {
                if (projectile instanceof AbstractArrowEntity && !(projectile instanceof KWItemEntity)) {
                    int kineticEnergy = projectile.getCapability(ProjectileUtilCapProvider.CAPABILITY).map(cap -> cap.getKineticEnergy()).orElse(0);
                    AbstractArrowEntity arrow = (AbstractArrowEntity) projectile;
                    if (kineticEnergy > 200.D) {
                        arrow.setCritArrow(true);
                    }
                }
                projectile.getCapability(ProjectileUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.addkineticEnergy());
                projectile.getCapability(ProjectileUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.setReadyToRelease(true));
            }
        });
    }
}

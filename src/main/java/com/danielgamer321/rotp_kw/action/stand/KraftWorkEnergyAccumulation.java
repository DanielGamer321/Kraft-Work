package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.capability.entity.EntityUtilCapProvider;
import com.danielgamer321.rotp_kw.capability.entity.ProjectileUtilCapProvider;
import com.danielgamer321.rotp_kw.entity.damaging.projectile.KWItemEntity;
import com.danielgamer321.rotp_kw.init.InitSounds;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.entity.stand.StandPose;
import com.github.standobyte.jojo.entity.stand.StandStatFormulas;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.MathUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;

public class KraftWorkEnergyAccumulation extends StandEntityAction {
    public static final StandPose GIVE_ENERGY_POSE = new StandPose("KW_GIVE_ENERGY");
    public KraftWorkEnergyAccumulation(Builder builder) {
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
    public int getStandWindupTicks(IStandPower standPower, StandEntity standEntity) {
        double speed = standEntity.getAttackSpeed();
        int ticks = StandStatFormulas.getLightAttackWindup(speed / 4, 0,
                0, standEntity.getCurrentTaskAction() != this);
        return ticks;
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        world.getEntitiesOfClass(ProjectileEntity.class, standEntity.getBoundingBox().inflate(standEntity.getAttributeValue(ForgeMod.REACH_DISTANCE.get())),
                entity -> standEntity.getLookAngle().dot(entity.getDeltaMovement().reverse().normalize()) <= MathHelper.cos((float) (30.0 +
                        MathHelper.clamp(standEntity.getPrecision(), 0, 16) * 30.0 / 16.0) * MathUtil.DEG_TO_RAD)).forEach(projectile -> {
            boolean PositionLocking = projectile.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
            if (PositionLocking) {
                if (projectile instanceof AbstractArrowEntity && !(projectile instanceof KWItemEntity)) {
                    int kineticEnergy = projectile.getCapability(ProjectileUtilCapProvider.CAPABILITY).map(cap -> cap.getKineticEnergy()).orElse(0);
                    AbstractArrowEntity arrow = (AbstractArrowEntity) projectile;
                    if (kineticEnergy > 200D) {
                        arrow.setCritArrow(true);
                    }
                }
                projectile.getCapability(ProjectileUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.addkineticEnergy());
                projectile.getCapability(ProjectileUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.setReadyToRelease(true));
                world.playSound(null, projectile, InitSounds.KRAFT_WORK_GIVING_KINETIC_ENERGY.get(), SoundCategory.PLAYERS, 1.0F, 1.0F / (world.random.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
            }
        });
    }

    @Override
    public int getStandRecoveryTicks(IStandPower standPower, StandEntity standEntity) {
        double speed = standEntity.getAttackSpeed();
        return StandStatFormulas.getLightAttackRecovery(speed, standEntity.getFinisherMeter())
                * (standEntity.isArmsOnlyMode() ? 2 : 4);
    }

    @Override
    protected boolean isCancelable(IStandPower standPower, StandEntity standEntity, @Nullable StandEntityAction newAction, Phase phase) {
        return phase == Phase.RECOVERY || super.isCancelable(standPower, standEntity, newAction, phase);
    }

    @Override
    protected boolean isChainable(IStandPower standPower, StandEntity standEntity) {
        return true;
    }
}

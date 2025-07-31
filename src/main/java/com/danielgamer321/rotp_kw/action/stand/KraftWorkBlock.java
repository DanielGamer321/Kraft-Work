package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.capability.entity.EntityUtilCapProvider;
import com.danielgamer321.rotp_kw.capability.entity.ProjectileUtilCapProvider;
import com.danielgamer321.rotp_kw.init.InitSounds;
import com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType;
import com.github.standobyte.jojo.action.stand.StandEntityBlock;
import com.github.standobyte.jojo.entity.RoadRollerEntity;
import com.github.standobyte.jojo.entity.damaging.projectile.ModdedProjectileEntity;
import com.github.standobyte.jojo.entity.damaging.projectile.TommyGunBulletEntity;
import com.github.standobyte.jojo.entity.damaging.projectile.ownerbound.SatiporojaScarfEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.*;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.projectile.EyeOfEnderEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

public class KraftWorkBlock extends StandEntityBlock {

    public KraftWorkBlock(Builder builder) {
        super(builder);
    }

    private boolean exceptionList(Entity projectile) {
        return
                projectile instanceof TommyGunBulletEntity ||
                !projectile.isPickable();
    }

    @Override
    public void standTickPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (userPower.getResolveLevel() >= 2) {
            LivingEntity user = userPower.getUser();
            world.getEntitiesOfClass(Entity.class, standEntity.getBoundingBox().inflate(standEntity.getAttributeValue(ForgeMod.REACH_DISTANCE.get())),
                    entity -> standEntity.getLookAngle().dot(entity.getDeltaMovement().reverse().normalize()) >= MathHelper.cos((float) (30.0 +
                            MathHelper.clamp(standEntity.getPrecision(), 0, 16) * 30.0 / 16.0) * MathUtil.DEG_TO_RAD)).forEach(entity -> {
                boolean PositionLocking = entity.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
                if (!PositionLocking) {
                    if (entity instanceof BoatEntity || entity instanceof AbstractMinecartEntity ||
                            entity instanceof TNTEntity || entity instanceof FallingBlockEntity ||
                            entity instanceof EyeOfEnderEntity || entity instanceof ArmorStandEntity ||
                            entity instanceof RoadRollerEntity) {
                        if (user.distanceToSqr(entity) < entity.getDeltaMovement().lengthSqr() * 34) {
                            LockTarget(user, entity);
                        }
                    }
                    else if (entity instanceof ProjectileEntity && exceptionList(entity)) {
                        if (user.distanceToSqr(entity) < 8) {
                            ProjectileEntity projectile = (ProjectileEntity) entity;
                            if (!(projectile instanceof ModdedProjectileEntity &&
                                    ((ModdedProjectileEntity) projectile).standDamage()) && projectile.getOwner() != user) {
                                if (userPower.getResolveLevel() >= 3) {
                                    projectile.shootFromRotation(user, user.xRot, user.yRot, 0.0F, 0.001F, 1.0F);
                                }
                                if (projectile instanceof TommyGunBulletEntity) {
                                    TommyGunBulletEntity oldBullet = (TommyGunBulletEntity) projectile;
                                    TommyGunBulletEntity newBullet = new TommyGunBulletEntity(user, world);
                                    newBullet.copyPosition(oldBullet);
                                    newBullet.setOwner(user);
                                    newBullet.shootFromRotation(standEntity, 20F, 0);
                                    world.addFreshEntity(newBullet);
                                    oldBullet.remove();
                                }
                                if (!(projectile instanceof EnderPearlEntity) || projectile instanceof SatiporojaScarfEntity) {
                                    projectile.setOwner(user);
                                }
                                LockTarget(user, projectile);
                                projectile.getCapability(ProjectileUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.setKineticEnergy(0));
                                world.playSound(null, standEntity, InitSounds.KRAFT_WORK_BLOCKS_A_PROJECTILE.get(), SoundCategory.AMBIENT,
                                        1.0F, 1.0F + (world.random.nextFloat() - 0.5F) * 0.15F);
                            }
                        }
                    }
                }
            });
        }
    }

    public void LockTarget(LivingEntity user, Entity target) {
        String lock_id = String.valueOf(user.getUUID());
        KraftWorkStandType.setPositionLockingServerSide(target, true);
        KraftWorkStandType.TagServerSide(target, lock_id, true);
        KraftWorkStandType.setCanUpdateServerSide(target, false);
        IStandPower.getStandPowerOptional(user).ifPresent(stand -> {
            stand.consumeStamina(10);
        });
    }
}

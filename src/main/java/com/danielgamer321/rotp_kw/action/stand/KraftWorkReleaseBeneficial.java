package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.capability.entity.EntityUtilCapProvider;
import com.danielgamer321.rotp_kw.entity.damaging.projectile.KWItemEntity;
import com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;

import java.util.List;

public class KraftWorkReleaseBeneficial extends StandAction {

    public KraftWorkReleaseBeneficial(Builder builder) {
        super(builder);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        String lock_id = String.valueOf(user.getUUID());
        world.getEntitiesOfClass(ProjectileEntity.class, user.getBoundingBox().inflate(12),
                entity -> entity.getTags().contains(lock_id)).forEach(projectile -> {
            boolean PositionLocking = projectile.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
            if (PositionLocking && BeneficialItems(projectile)) {
                KraftWorkStandType.setPositionLockingServerSide(projectile, false);
            }
        });
    }

    public static boolean BeneficialItems (ProjectileEntity projectile) {
        if (projectile instanceof PotionEntity) {
            PotionEntity potionP = (PotionEntity) projectile;
            ItemStack item = potionP.getItem();
            List<EffectInstance> list = PotionUtils.getMobEffects(item);
            if (!list.isEmpty()){
                for(EffectInstance effectinstance : list) {
                    Effect effect = effectinstance.getEffect();
                    return effect.isBeneficial();
                }
            }
        }
        else if (projectile instanceof KWItemEntity) {
            KWItemEntity item = (KWItemEntity) projectile;
            return item.isBeneficial();
        }
        return false;
    }

}

package com.danielgamer321.rotp_kw.potion;

import com.danielgamer321.rotp_kw.init.InitEffects;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.potion.IApplicableEffect;
import com.github.standobyte.jojo.potion.ImmobilizeEffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import static com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType.setPositionLockingServerSide;

public class LockedPositionEffect extends ImmobilizeEffect implements IApplicableEffect {

    public LockedPositionEffect(int liquidColor) {
        super(liquidColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.abilities.flying = false;
        }
        if (resetsDeltaMovement()) {
            entity.setDeltaMovement(0, 0, 0);
        }
        if (!entity.hasEffect(InitEffects.TRANSPORT_LOCKED.get())) {
            entity.stopRiding();
        }
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeModifierManager modifiers, int amplifier) {
        super.addAttributeModifiers(entity, modifiers, amplifier);
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (InitEffects.lockedInventory(player)) {
                player.closeContainer();
            }
        }
        entity.setNoGravity(true);
        entity.setDeltaMovement(Vector3d.ZERO);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeModifierManager modifiers, int amplifier) {
        super.removeAttributeModifiers(entity, modifiers, amplifier);
        entity.setNoGravity(false);
        entity.fallDistance = 0.0F;
        if (!InitEffects.isLocked(entity)) {
            setPositionLockingServerSide(entity, false);
        }
    }

    @Override
    public boolean isApplicable(LivingEntity entity) {
        return super.isApplicable(entity) && !(entity instanceof StandEntity);
    }
}

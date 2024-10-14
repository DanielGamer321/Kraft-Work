package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.capability.entity.EntityUtilCapProvider;
import com.danielgamer321.rotp_kw.init.InitEffects;
import com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.punch.IPunch;
import com.github.standobyte.jojo.action.stand.punch.StandEntityPunch;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.horse.TraderLlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public class KraftWorkLockArmor extends KWActionModifier {

    public KraftWorkLockArmor(Builder builder) {
        super(builder);
    }
    
    @Override
    public boolean isUnlocked(IStandPower power) {
        return power.getResolveLevel() >= 1;
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Entity entity = target.getEntity();
        if (entity != null) {
            boolean PositionLocking = entity.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
            if (!PositionLocking) {
                if (entity instanceof LivingEntity) {
                    LivingEntity targetEntity = (LivingEntity) entity;
                    if (targetEntity instanceof StandEntity || (targetEntity instanceof PlayerEntity &&
                            ((PlayerEntity)targetEntity).abilities.instabuild)) {
                        return ActionConditionResult.NEGATIVE;
                    }
                    else if (!EntitiesWithItems(targetEntity)) {
                        return ActionConditionResult.NEGATIVE;
                    }
                }
            }
            else {
                return ActionConditionResult.NEGATIVE;
            }
        }
        else {
            return ActionConditionResult.NEGATIVE;
        }
        return super.checkSpecificConditions(user, power, target);
    }

    private boolean EntitiesWithItems(LivingEntity target) {
        ItemStack main = target.getItemBySlot(EquipmentSlotType.MAINHAND);
        ItemStack off = target.getItemBySlot(EquipmentSlotType.OFFHAND);
        ItemStack helmet = target.getItemBySlot(EquipmentSlotType.HEAD);
        ItemStack chestplace = target.getItemBySlot(EquipmentSlotType.CHEST);
        ItemStack leggings = target.getItemBySlot(EquipmentSlotType.LEGS);
        ItemStack boots = target.getItemBySlot(EquipmentSlotType.FEET);
        return !helmet.isEmpty() || !chestplace.isEmpty() || !leggings.isEmpty() || !boots.isEmpty() ||
                (target.getUseItem().isShield(target) && !(main.isEmpty() && off.isEmpty())) ||
                (target instanceof RavagerEntity) ||
                (target instanceof LlamaEntity && ((LlamaEntity)target).getSwag() != null) ||
                (target instanceof TraderLlamaEntity) ||
                (target instanceof PigEntity && ((PigEntity)target).isSaddled()) ||
                (target instanceof StriderEntity && ((StriderEntity)target).isSaddled()) ||
                (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isSaddled()) ||
                (target instanceof AbstractChestedHorseEntity && ((AbstractChestedHorseEntity)target).hasChest());
    }

    @Override
    public void standTickRecovery(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        boolean triggerEffect = task.getTicksLeft() <= 1;
        LivingEntity user = userPower.getUser();
        String lock_id = String.valueOf(user.getUUID());
        if (task.getAdditionalData().isEmpty(TriggeredFlag.class) && task.getTarget().getType() == ActionTarget.TargetType.ENTITY) {
            Entity entity = task.getTarget().getEntity();
            if (entity.isAlive() && entity instanceof LivingEntity && !(entity instanceof StandEntity)) {
                if (!world.isClientSide() && triggerEffect) {
                    LivingEntity targetEntity = (LivingEntity) entity;
                    if (EntitiesWithItems(targetEntity)) {
                        ItemStack main = targetEntity.getItemBySlot(EquipmentSlotType.MAINHAND);
                        ItemStack off = targetEntity.getItemBySlot(EquipmentSlotType.OFFHAND);
                        ItemStack helmet = targetEntity.getItemBySlot(EquipmentSlotType.HEAD);
                        ItemStack chestplace = targetEntity.getItemBySlot(EquipmentSlotType.CHEST);
                        ItemStack leggings = targetEntity.getItemBySlot(EquipmentSlotType.LEGS);
                        ItemStack boots = targetEntity.getItemBySlot(EquipmentSlotType.FEET);
                        IStandPower.getStandPowerOptional(user).ifPresent(stand -> {
                            IPunch punch = standEntity.getLastPunch();
                            float damageDealt = punch.getType() == ActionTarget.TargetType.ENTITY ? ((StandEntityPunch) punch).getDamageDealtToLiving() : 0;
                            if (targetEntity.getUseItem().isShield(targetEntity) && damageDealt == 0) {
                                if (main.getItem() instanceof ShieldItem) {
                                    targetEntity.addEffect(new EffectInstance(InitEffects.LOCKED_MAIN_HAND.get(), 19999980, 0, false, false, true));
                                    stand.consumeStamina(5);
                                }
                                else if (off.getItem() instanceof ShieldItem) {
                                    targetEntity.addEffect(new EffectInstance(InitEffects.LOCKED_OFF_HAND.get(), 19999980, 0, false, false, true));
                                    stand.consumeStamina(5);
                                }
                            }
                            else {
                                if (!helmet.isEmpty()) {
                                    binding(user, false, helmet);
                                    targetEntity.addEffect(new EffectInstance(InitEffects.LOCKED_HELMET.get(), 19999980, 0, false, false, true));
                                    stand.consumeStamina(5);
                                }
                                if (!chestplace.isEmpty()) {
                                    if (targetEntity instanceof AbstractHorseEntity) {
                                        targetEntity.addEffect(new EffectInstance(InitEffects.FULL_TRANSPORT_LOCKED.get(), 19999980, 0, false, false, true));
                                        stand.consumeStamina(5);
                                    }
                                    else {
                                        binding(user, false, chestplace);
                                        targetEntity.addEffect(new EffectInstance(InitEffects.LOCKED_CHESTPLATE.get(), 19999980, 0, false, false, true));
                                        stand.consumeStamina(5);
                                    }
                                }
                                if (!leggings.isEmpty()) {
                                    binding(user, false, leggings);
                                    targetEntity.addEffect(new EffectInstance(InitEffects.LOCKED_LEGGINGS.get(), 19999980, 0, false, false, true));
                                    stand.consumeStamina(5);
                                }
                                if (!boots.isEmpty()) {
                                    binding(user, false, boots);
                                    targetEntity.addEffect(new EffectInstance(InitEffects.LOCKED_POSITION.get(), 19999980, 0, false, false, true));
                                    stand.consumeStamina(5);
                                }
                                if (targetEntity instanceof RavagerEntity ||
                                        targetEntity instanceof PigEntity && ((PigEntity)targetEntity).isSaddled() ||
                                        targetEntity instanceof AbstractChestedHorseEntity && ((AbstractChestedHorseEntity)targetEntity).hasChest()) {
                                    targetEntity.addEffect(new EffectInstance(InitEffects.TRANSPORT_LOCKED.get(), 19999980, 0, false, false, true));
                                    stand.consumeStamina(5);
                                }
                                if (targetEntity instanceof StriderEntity && ((StriderEntity)targetEntity).isSaddled() ||
                                        targetEntity instanceof AbstractHorseEntity && ((AbstractHorseEntity)targetEntity).isSaddled() ||
                                        targetEntity instanceof LlamaEntity && ((LlamaEntity)targetEntity).getSwag() != null ||
                                        targetEntity instanceof TraderLlamaEntity) {
                                    targetEntity.addEffect(new EffectInstance(InitEffects.FULL_TRANSPORT_LOCKED.get(), 19999980, 0, false, false, true));
                                    stand.consumeStamina(5);
                                }
                            }
                        });
                        KraftWorkStandType.setPositionLockingServerSide(entity, true);
                        KraftWorkStandType.TagServerSide(entity, lock_id, true);
                    }
                }
            }
        }
        if (triggerEffect) {
            task.getAdditionalData().push(TriggeredFlag.class, new TriggeredFlag());
        }
    }

    public void binding(LivingEntity user, Boolean status, ItemStack armor) {
        KraftWorkLockYourself.binding(user, status, armor);
    }
}

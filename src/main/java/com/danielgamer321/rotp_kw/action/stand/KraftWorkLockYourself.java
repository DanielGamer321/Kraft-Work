package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.capability.entity.EntityUtilCapProvider;
import com.danielgamer321.rotp_kw.init.InitEffects;
import com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.LazySupplier;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Stream;

public class KraftWorkLockYourself extends StandAction {
    public KraftWorkLockYourself(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        ItemStack main = user.getItemBySlot(EquipmentSlotType.MAINHAND);
        ItemStack helmet = user.getItemBySlot(EquipmentSlotType.HEAD);
        ItemStack chestplace = user.getItemBySlot(EquipmentSlotType.CHEST);
        ItemStack leggings = user.getItemBySlot(EquipmentSlotType.LEGS);
        ItemStack boots = user.getItemBySlot(EquipmentSlotType.FEET);
        if (user instanceof PlayerEntity && ((PlayerEntity)user).abilities.instabuild) {
            return conditionMessage("you_are_not_in_survival");
        }
        else if(main.isEmpty() && helmet.isEmpty() && chestplace.isEmpty() &&
                leggings.isEmpty() && boots.isEmpty()) {
            return ActionConditionResult.NEGATIVE;
        }
        return super.checkSpecificConditions(user, power, target);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide()) {
            boolean PositionLocking = user.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
            ItemStack main = user.getItemBySlot(EquipmentSlotType.MAINHAND);
            ItemStack helmet = user.getItemBySlot(EquipmentSlotType.HEAD);
            ItemStack chestplace = user.getItemBySlot(EquipmentSlotType.CHEST);
            ItemStack leggings = user.getItemBySlot(EquipmentSlotType.LEGS);
            ItemStack boots = user.getItemBySlot(EquipmentSlotType.FEET);
            if (!PositionLocking) {
                if (!helmet.isEmpty() || !chestplace.isEmpty() || ! leggings.isEmpty() || !boots.isEmpty()) {
                    if (!helmet.isEmpty()) {
                        binding(user, false, helmet);
                        user.addEffect(new EffectInstance(InitEffects.LOCKED_HELMET.get(), 19999980, 0, false, false, true));
                    }
                    if (!chestplace.isEmpty()) {
                        binding(user, false, chestplace);
                        user.addEffect(new EffectInstance(InitEffects.LOCKED_CHESTPLATE.get(), 19999980, 0, false, false, true));
                    }
                    if (!leggings.isEmpty()) {
                        binding(user, false, leggings);
                        user.addEffect(new EffectInstance(InitEffects.LOCKED_LEGGINGS.get(), 19999980, 0, false, false, true));
                    }
                    if (!boots.isEmpty()) {
                        binding(user, false, boots);
                        user.addEffect(new EffectInstance(InitEffects.LOCKED_POSITION.get(), 19999980, 0, false, false, true));
                    }
                }
                else {
                    if (!main.isEmpty()) {
                        user.addEffect(new EffectInstance(InitEffects.LOCKED_MAIN_HAND.get(), 19999980, 0, false, false, true));
                    }
                }
                KraftWorkStandType.setPositionLockingServerSide(user, true);
            }
            else {
                user.removeEffect(InitEffects.LOCKED_MAIN_HAND.get());
                user.removeEffect(InitEffects.LOCKED_OFF_HAND.get());
                binding(user, true, helmet);
                user.removeEffect(InitEffects.LOCKED_HELMET.get());
                binding(user, true, chestplace);
                user.removeEffect(InitEffects.LOCKED_CHESTPLATE.get());
                binding(user, true, leggings);
                user.removeEffect(InitEffects.LOCKED_LEGGINGS.get());
                binding(user, true, boots);
                user.removeEffect(InitEffects.LOCKED_POSITION.get());
                KraftWorkStandType.setPositionLockingServerSide(user, false);
            }
        }
    }

    public static void binding(LivingEntity user, Boolean status, ItemStack armor) {
        String lock_id = String.valueOf(user.getUUID());
        if (!status && !EnchantmentHelper.hasBindingCurse(armor)) {
            ItemStack copy = armor.copy();
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(armor);
            Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(copy);
            map1.put(Enchantments.BINDING_CURSE, 1);
            for(Enchantment enchantment : map1.keySet()) {
                int i2 = map.getOrDefault(enchantment, 0);
                int j2 = map1.get(enchantment);
                j2 = Math.max(j2, i2);
                if (j2 > enchantment.getMaxLevel()) {
                    j2 = enchantment.getMaxLevel();
                }
                map.put(enchantment, j2);
            }
            copy.shrink(1);
            armor.getOrCreateTagElement(lock_id).putUUID(lock_id, user.getUUID());
            EnchantmentHelper.setEnchantments(map, armor);
        }
        else if (EnchantmentHelper.hasBindingCurse(armor) && armor.getOrCreateTagElement(lock_id ).contains(lock_id )) {
            ItemStack copy = armor.copy();
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(armor);
            Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(copy);
            map1.put(Enchantments.BINDING_CURSE, 1);
            for(Enchantment enchantment : map1.keySet()) {
                map.remove(Enchantments.BINDING_CURSE);
            }
            copy.shrink(1);
            armor.removeTagKey(lock_id);
            EnchantmentHelper.setEnchantments(map, armor);
        }
    }

    @Override
    public float getStaminaCost(IStandPower stand) {
        LivingEntity user = stand.getUser();
        int multiplier = 0;
        boolean PositionLocking = user.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
        ItemStack main = user.getItemBySlot(EquipmentSlotType.MAINHAND);
        ItemStack helmet = user.getItemBySlot(EquipmentSlotType.HEAD);
        ItemStack chestplace = user.getItemBySlot(EquipmentSlotType.CHEST);
        ItemStack leggings = user.getItemBySlot(EquipmentSlotType.LEGS);
        ItemStack boots = user.getItemBySlot(EquipmentSlotType.FEET);
        if (!helmet.isEmpty() || !chestplace.isEmpty() || ! leggings.isEmpty() || !boots.isEmpty()) {
            if (!helmet.isEmpty()) {
                multiplier = multiplier + 1;
            }
            if (!chestplace.isEmpty()) {
                multiplier = multiplier + 1;
            }
            if (!leggings.isEmpty()) {
                multiplier = multiplier + 1;
            }
            if (!boots.isEmpty()) {
                multiplier = multiplier + 1;
            }
        }
        else if (!main.isEmpty()) {
            multiplier = multiplier + 1;
        }
        if (PositionLocking) {
            return 10F * multiplier;
        }
        else {
            return 1F * multiplier;
        }
    }

    private final LazySupplier<ResourceLocation> altTex =
            new LazySupplier<>(() -> makeIconVariant(this, "_on"));
    @Override
    public ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        if (power != null && getStatus(power)) {
            return altTex.get();
        }
        else {
            return super.getIconTexturePath(power);
        }
    }

    private boolean getStatus(IStandPower power) {
        LivingEntity user = power.getUser();
        return user.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
    }



    @Deprecated
    private ResourceLocation altTexPath;
    @Deprecated
    @Override
    public ResourceLocation getTexture(IStandPower power) {
        ResourceLocation resLoc = getRegistryName();
        if (getStatus(power)) {
            if (altTexPath == null) {
                altTexPath = new ResourceLocation(resLoc.getNamespace(), resLoc.getPath() + "_on");
            }
            resLoc = altTexPath;
        }
        return resLoc;
    }

    @Deprecated
    @Override
    public Stream<ResourceLocation> getTexLocationstoLoad() {
        ResourceLocation resLoc = getRegistryName();
        return Stream.of(resLoc, new ResourceLocation(resLoc.getNamespace(), resLoc.getPath() + "_on"));
    }
}

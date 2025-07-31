package com.danielgamer321.rotp_kw.init;

import com.danielgamer321.rotp_kw.RotpKraftWorkAddon;
import com.danielgamer321.rotp_kw.potion.*;
import com.github.standobyte.jojo.potion.ImmobilizeEffect;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

@EventBusSubscriber(modid = RotpKraftWorkAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class InitEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, RotpKraftWorkAddon.MOD_ID);
    
    public static final RegistryObject<ImmobilizeEffect> LOCKED_POSITION = EFFECTS.register("locked_position",
            () -> new LockedPositionEffect(0xDB4736).setUncurable());

    public static final RegistryObject<ImmobilizeEffect> LOCKED_MAIN_HAND = EFFECTS.register("locked_main_hand",
            () -> new LockedHandsEffect(0xDB4736).setUncurable());

    public static final RegistryObject<ImmobilizeEffect> LOCKED_OFF_HAND = EFFECTS.register("locked_off_hand",
            () -> new LockedHandsEffect(0xDB4736).setUncurable());

    public static final RegistryObject<ImmobilizeEffect> LOCKED_HELMET = EFFECTS.register("locked_helmet",
            () -> new LockedPositionEffect(0xDB4736).setUncurable());

    public static final RegistryObject<ImmobilizeEffect> LOCKED_CHESTPLATE = EFFECTS.register("locked_chestplate",
            () -> new LockedPositionEffect(0xDB4736).setUncurable());

    public static final RegistryObject<ImmobilizeEffect> LOCKED_LEGGINGS = EFFECTS.register("locked_leggings",
            () -> new LockedPositionEffect(0xDB4736).setUncurable());

    public static final RegistryObject<ImmobilizeEffect> TRANSPORT_LOCKED = EFFECTS.register("transportation_locked",
            () -> new LockedPositionEffect(0xDB4736).setUncurable());

    public static final RegistryObject<ImmobilizeEffect> FULL_TRANSPORT_LOCKED = EFFECTS.register("full_transportation_locked",
            () -> new TransportLockedEffect(0xDB4736).setUncurable());

    private static Set<Effect> TRACKED_EFFECTS;
    @SubscribeEvent(priority = EventPriority.LOW)
    public static final void afterEffectsRegister(RegistryEvent.Register<Effect> event) {
        TRACKED_EFFECTS = ImmutableSet.of(
                LOCKED_POSITION.get(),
                LOCKED_MAIN_HAND.get(),
                LOCKED_OFF_HAND.get(),
                LOCKED_HELMET.get(),
                LOCKED_CHESTPLATE.get(),
                LOCKED_LEGGINGS.get(),
                TRANSPORT_LOCKED.get(),
                FULL_TRANSPORT_LOCKED.get()
        );
    }

    public static boolean isEffectTracked(Effect effect) {
        return TRACKED_EFFECTS.contains(effect);
    }

    public static boolean isLocked(LivingEntity entity) {
        return entity.hasEffect(LOCKED_POSITION.get()) || lockedArms(entity) ||
                shiftLocked(entity);
    }

    public static boolean lockIA(LivingEntity entity) {
        return lockedArms(entity) || entity.hasEffect(TRANSPORT_LOCKED.get()) ||
                entity.hasEffect(FULL_TRANSPORT_LOCKED.get());
    }

    public static boolean lockedArms(LivingEntity entity) {
        return entity.hasEffect(LOCKED_MAIN_HAND.get()) || entity.hasEffect(LOCKED_OFF_HAND.get());
    }

    public static boolean shiftLocked(LivingEntity entity) {
        return entity.hasEffect(LOCKED_HELMET.get()) || entity.hasEffect(LOCKED_CHESTPLATE.get()) ||
                entity.hasEffect(LOCKED_LEGGINGS.get()) || entity.hasEffect(TRANSPORT_LOCKED.get()) ||
                entity.hasEffect(FULL_TRANSPORT_LOCKED.get());
    }

    public static boolean lockedInventory(LivingEntity entity) {
        return lockedArms(entity) || entity.hasEffect(LOCKED_CHESTPLATE.get()) ||
                entity.hasEffect(LOCKED_LEGGINGS.get()) || entity.hasEffect(TRANSPORT_LOCKED.get()) ||
                entity.hasEffect(FULL_TRANSPORT_LOCKED.get());
    }
}

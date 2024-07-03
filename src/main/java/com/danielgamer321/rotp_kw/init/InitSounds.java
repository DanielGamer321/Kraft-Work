package com.danielgamer321.rotp_kw.init;

import com.danielgamer321.rotp_kw.RotpKraftWorkAddon;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.util.mc.OstSoundList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RotpKraftWorkAddon.MOD_ID);

    public static final RegistryObject<SoundEvent> SALE_KRAFT_WORK = SOUNDS.register("sale_kraft_work",
            () -> new SoundEvent(new ResourceLocation(RotpKraftWorkAddon.MOD_ID, "sale_kraft_work")));

    public static final Supplier<SoundEvent> KRAFT_WORK_SUMMON = ModSounds.STAND_SUMMON_DEFAULT;

    public static final Supplier<SoundEvent> KRAFT_WORK_UNSUMMON = ModSounds.STAND_UNSUMMON_DEFAULT;

    public static final Supplier<SoundEvent> KRAFT_WORK_PUNCH_LIGHT = ModSounds.STAND_PUNCH_LIGHT;

    public static final Supplier<SoundEvent> KRAFT_WORK_PUNCH_HEAVY = ModSounds.STAND_PUNCH_HEAVY;

    public static final Supplier<SoundEvent> KRAFT_WORK_BARRAGE = ModSounds.STAND_PUNCH_LIGHT;

    public static final RegistryObject<SoundEvent> KRAFT_WORK_BLOCKS_A_PROJECTILE = SOUNDS.register("kraft_work_blocks_a_projectile",
            () -> new SoundEvent(new ResourceLocation(RotpKraftWorkAddon.MOD_ID, "kraft_work_blocks_a_projectile")));

    public static final RegistryObject<SoundEvent> KRAFT_WORK_RELEASED_PROJECTILE = SOUNDS.register("kraft_work_released_projectile",
            () -> new SoundEvent(new ResourceLocation(RotpKraftWorkAddon.MOD_ID, "kraft_work_released_projectile")));

    static final OstSoundList KRAFT_WORK_OST = new OstSoundList(new ResourceLocation(RotpKraftWorkAddon.MOD_ID, "kraft_work_ost"), SOUNDS);

}

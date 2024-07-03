package com.danielgamer321.rotp_kw.util;

import com.danielgamer321.rotp_kw.RotpKraftWorkAddon;
import com.danielgamer321.rotp_kw.capability.entity.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = RotpKraftWorkAddon.MOD_ID)
public class ForgeBusEventSubscriber {
    private static final ResourceLocation PLAYER_UTIL_CAP = new ResourceLocation(RotpKraftWorkAddon.MOD_ID, "player_util");
    private static final ResourceLocation ENTITY_UTIL_CAP = new ResourceLocation(RotpKraftWorkAddon.MOD_ID, "entity_util");
    private static final ResourceLocation PROJECTIL_UTIL_CAP = new ResourceLocation(RotpKraftWorkAddon.MOD_ID, "projectile_util");

    
    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        event.addCapability(ENTITY_UTIL_CAP, new EntityUtilCapProvider(entity));
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            event.addCapability(PLAYER_UTIL_CAP, new PlayerUtilCapProvider(player));
        }
        if (entity instanceof ProjectileEntity) {
            event.addCapability(PROJECTIL_UTIL_CAP, new ProjectileUtilCapProvider(entity));
        }
    }
    
    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(PlayerUtilCap.class, new PlayerUtilCapStorage(), () -> new PlayerUtilCap(null));
        CapabilityManager.INSTANCE.register(EntityUtilCap.class, new EntityUtilCapStorage(), () -> new EntityUtilCap(null));
        CapabilityManager.INSTANCE.register(ProjectileUtilCap.class, new ProjectileUtilCapStorage(), () -> new ProjectileUtilCap(null));
    }
}

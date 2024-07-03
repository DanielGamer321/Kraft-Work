package com.danielgamer321.rotp_kw.client;

import com.danielgamer321.rotp_kw.RotpKraftWorkAddon;
import com.danielgamer321.rotp_kw.client.render.entity.renderer.KWBlockRenderer;
import com.danielgamer321.rotp_kw.client.render.entity.renderer.damaging.projectile.KWItemRenderer;
import com.danielgamer321.rotp_kw.client.render.entity.renderer.stand.*;
import com.danielgamer321.rotp_kw.init.AddonStands;
import com.danielgamer321.rotp_kw.init.InitEntities;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = RotpKraftWorkAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {

    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        Minecraft mc = event.getMinecraftSupplier().get();

        RenderingRegistry.registerEntityRenderingHandler(InitEntities.KW_ITEM.get(), KWItemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(InitEntities.KW_BLOCK.get(), KWBlockRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(AddonStands.KRAFT_WORK.getEntityType(), KraftWorkRenderer::new);

        InputHandler.init(mc);
    }
}

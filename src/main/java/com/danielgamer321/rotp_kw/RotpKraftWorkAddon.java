package com.danielgamer321.rotp_kw;

import com.danielgamer321.rotp_kw.init.*;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RotpKraftWorkAddon.MOD_ID)
public class RotpKraftWorkAddon {
    // The value here should match an entry in the META-INF/mods.toml file
    public static final String MOD_ID = "rotp_kw";
    private static final Logger LOGGER = LogManager.getLogger();

    public RotpKraftWorkAddon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InitEntities.ENTITIES.register(modEventBus);
        InitSounds.SOUNDS.register(modEventBus);
        InitStands.ACTIONS.register(modEventBus);
        InitStands.STANDS.register(modEventBus);
        InitEffects.EFFECTS.register(modEventBus);
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}

package com.danielgamer321.rotp_kw.init;

import com.danielgamer321.rotp_kw.RotpKraftWorkAddon;
import com.danielgamer321.rotp_kw.entity.KWBlockEntity;
import com.danielgamer321.rotp_kw.entity.damaging.projectile.KWItemEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, RotpKraftWorkAddon.MOD_ID);


    public static final RegistryObject<EntityType<KWItemEntity>> KW_ITEM = ENTITIES.register("kw_item",
            () -> EntityType.Builder.<KWItemEntity>of(KWItemEntity::new, EntityClassification.MISC).sized(0.25F, 0.25F).noSummon().noSave().setUpdateInterval(20)
                    .build(new ResourceLocation(RotpKraftWorkAddon.MOD_ID, "kw_item").toString()));



    public static final RegistryObject<EntityType<KWBlockEntity>> KW_BLOCK = ENTITIES.register("kw_block",
            () -> EntityType.Builder.<KWBlockEntity>of(KWBlockEntity::new, EntityClassification.MISC).sized(1.0F, 1.0F).setUpdateInterval(Integer.MAX_VALUE).setShouldReceiveVelocityUpdates(false).fireImmune()
                    .build(new ResourceLocation(RotpKraftWorkAddon.MOD_ID, "kw_block").toString()));
}

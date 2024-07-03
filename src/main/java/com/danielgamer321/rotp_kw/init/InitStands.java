package com.danielgamer321.rotp_kw.init;

import com.danielgamer321.rotp_kw.RotpKraftWorkAddon;
import com.danielgamer321.rotp_kw.action.stand.*;
import com.danielgamer321.rotp_kw.entity.stand.stands.*;
import com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.*;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.power.impl.stand.StandInstance.StandPart;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.github.standobyte.jojo.util.mod.StoryPart;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import static com.github.standobyte.jojo.init.ModEntityTypes.ENTITIES;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), RotpKraftWorkAddon.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), RotpKraftWorkAddon.MOD_ID);

 // ======================================== Kraft Work ========================================

    public static final RegistryObject<StandEntityAction> KRAFT_WORK_PUNCH = ACTIONS.register("kraft_work_punch",
            () -> new StandEntityLightAttack(new StandEntityLightAttack.Builder()
                    .punchSound(InitSounds.KRAFT_WORK_PUNCH_LIGHT)));

    public static final RegistryObject<StandEntityAction> KRAFT_WORK_BARRAGE = ACTIONS.register("kraft_work_barrage",
            () -> new StandEntityMeleeBarrage(new StandEntityMeleeBarrage.Builder()
                    .barrageHitSound(InitSounds.KRAFT_WORK_BARRAGE)));

    public static final RegistryObject<StandEntityHeavyAttack> KRAFT_WORK_COMBO_PUNCH = ACTIONS.register("kraft_work_combo_punch",
            () -> new KraftWorkComboPunch(new StandEntityHeavyAttack.Builder()
                    .resolveLevelToUnlock(1)
                    .punchSound(InitSounds.KRAFT_WORK_PUNCH_HEAVY)
                    .partsRequired(StandPart.ARMS)));

    public static final RegistryObject<StandEntityActionModifier> KRAFT_WORK_LOCK_ARMOR = ACTIONS.register("kraft_work_lock_armor",
            () -> new KraftWorkLockArmor(new StandAction.Builder()));

    public static final RegistryObject<KraftWorkHeavyPunch> KRAFT_WORK_HEAVY_PUNCH = ACTIONS.register("kraft_work_heavy_punch",
            () -> new KraftWorkHeavyPunch(new StandEntityHeavyAttack.Builder()
                    .punchSound(InitSounds.KRAFT_WORK_PUNCH_HEAVY)
                    .partsRequired(StandPart.ARMS)
                    .setFinisherVariation(KRAFT_WORK_COMBO_PUNCH)
                    .shiftVariationOf(KRAFT_WORK_PUNCH).shiftVariationOf(KRAFT_WORK_BARRAGE)));

    public static final RegistryObject<StandAction> KRAFT_WORK_PlACE_PROJECTILE = ACTIONS.register("kraft_work_place_projectile",
            () -> new KraftWorkPlaceProjectile(new StandAction.Builder().staminaCost(10F)
                    .resolveLevelToUnlock(2)));

    public static final RegistryObject<StandEntityAction> KRAFT_WORK_BLOCK = ACTIONS.register("kraft_work_block",
            () -> new KraftWorkBlock(new StandEntityBlock.Builder()));

    public static final RegistryObject<StandAction> KRAFT_WORK_LOCK_YOURSELF = ACTIONS.register("kraft_work_lock_yourself",
            () -> new KraftWorkLockYourself(new StandAction.Builder()
                    .resolveLevelToUnlock(1)));

    public static final RegistryObject<StandAction> KRAFT_WORK_LOCK_TARGET = ACTIONS.register("kraft_work_lock_target",
            () -> new KraftWorkLockTarget(new StandAction.Builder().staminaCost(10).holdToFire(10, false)
                    .resolveLevelToUnlock(1)));

    public static final RegistryObject<StandAction> KRAFT_WORK_RELEASE_TARGET = ACTIONS.register("kraft_work_release_target",
            () -> new KraftWorkReleaseTarget(new StandAction.Builder().staminaCost(1)
                    .resolveLevelToUnlock(1)
                    .ignoresPerformerStun()
                    .shiftVariationOf(KRAFT_WORK_LOCK_TARGET)));

    public static final RegistryObject<StandEntityAction> KRAFT_WORK_ENERGY_ACCUMULATION = ACTIONS.register("kraft_work_energy_accumulation",
            () -> new KraftWorkEnergyAccumulation(new StandEntityAction.Builder().staminaCostTick(0.1F).standUserWalkSpeed(0.0F).holdType()
                    .resolveLevelToUnlock(2)
                    .standOffsetFront().standPose(KraftWorkEnergyAccumulation.GIVE_ENERGY_POSE)
                    .partsRequired(StandPart.ARMS)));

    public static final RegistryObject<StandAction> KRAFT_WORK_RELEASE_PROJECTILE = ACTIONS.register("kraft_work_release_projectile",
            () -> new KraftWorkReleaseProjectile(new StandAction.Builder()
                    .resolveLevelToUnlock(2)
                    .ignoresPerformerStun()));

    public static final RegistryObject<StandAction> KRAFT_WORK_RELEASE_ALL_PROJECTILES = ACTIONS.register("kraft_work_release_all_projectiles",
            () -> new KraftWorkReleaseAllProjectiles(new StandAction.Builder()
                    .resolveLevelToUnlock(2)
                    .ignoresPerformerStun()));

    public static final RegistryObject<StandAction> KRAFT_WORK_RELEASE_PROJECTILES_R = ACTIONS.register("kraft_work_release_projectiles_r",
            () -> new KraftWorkReleaseProjectilesR(new StandAction.Builder()
                    .resolveLevelToUnlock(2)
                    .ignoresPerformerStun()));

    public static final RegistryObject<StandAction> KRAFT_WORK_RELEASE_PROJECTILES_NR = ACTIONS.register("kraft_work_release_projectiles_nr",
            () -> new KraftWorkReleaseProjectilesNR(new StandAction.Builder()
                    .resolveLevelToUnlock(2)
                    .ignoresPerformerStun()));

    public static final RegistryObject<StandAction> KRAFT_WORK_RELEASE_ENDER_PEARL = ACTIONS.register("kraft_work_release_ender_pearl",
            () -> new KraftWorkReleaseEnderPearl(new StandAction.Builder()
                    .resolveLevelToUnlock(2)
                    .ignoresPerformerStun()));

    public static final RegistryObject<StandAction> KRAFT_WORK_RELEASE_BENEFICIAL = ACTIONS.register("kraft_work_release_beneficial",
            () -> new KraftWorkReleaseBeneficial(new StandAction.Builder()
                    .resolveLevelToUnlock(2)
                    .ignoresPerformerStun()));

    public static final RegistryObject<StandAction> KRAFT_WORK_ADVANCED_RELEASE = ACTIONS.register("kraft_work_advanced_release",
            () -> new KraftWorkAdvancedRelease(new StandAction.Builder()
                    .resolveLevelToUnlock(2)
                    .ignoresPerformerStun()
                    .shiftVariationOf(KRAFT_WORK_RELEASE_PROJECTILE)));

    public static final RegistryObject<StandAction> KRAFT_WORK_BI_STATUS = ACTIONS.register("kraft_work_bi_status",
            () -> new KraftWorkBlockingItemsStatus(new StandAction.Builder()
                    .resolveLevelToUnlock(3).isTrained()
                    .ignoresPerformerStun()));


    public static final EntityStandRegistryObject<KraftWorkStandType<StandStats>, StandEntityType<KraftWorkEntity>> STAND_KRAFT_WORK =
            new EntityStandRegistryObject<>("kraft_work",
                    STANDS,
                    () -> new KraftWorkStandType.Builder<>()
                            .color(0xA4C686)
                            .storyPartName(StoryPart.GOLDEN_WIND.getName())
                            .leftClickHotbar(
                                    KRAFT_WORK_PUNCH.get(),
                                    KRAFT_WORK_BARRAGE.get(),
                                    KRAFT_WORK_PlACE_PROJECTILE.get()
                            )
                            .rightClickHotbar(
                                    KRAFT_WORK_BLOCK.get(),
                                    KRAFT_WORK_LOCK_YOURSELF.get(),
                                    KRAFT_WORK_LOCK_TARGET.get(),
                                    KRAFT_WORK_ENERGY_ACCUMULATION.get(),
                                    KRAFT_WORK_RELEASE_PROJECTILE.get(),
                                    KRAFT_WORK_BI_STATUS.get()
                            )
                            .defaultStats(StandStats.class, new StandStats.Builder()
                                    .power(14.0)
                                    .speed(14.0)
                                    .range(2.0, 2.0)
                                    .durability(10.0)
                                    .precision(2.0)
                                    .randomWeight(2)
                            )
                            .addSummonShout(InitSounds.SALE_KRAFT_WORK)
                            .addOst(InitSounds.KRAFT_WORK_OST)
                            .build(),

                    ENTITIES,
                    () -> new StandEntityType<KraftWorkEntity>(KraftWorkEntity::new, 0.65F, 1.95F)
                            .summonSound(InitSounds.KRAFT_WORK_SUMMON)
                            .unsummonSound(InitSounds.KRAFT_WORK_UNSUMMON))
                    .withDefaultStandAttributes();
}

package com.danielgamer321.rotp_kw.util;

import com.danielgamer321.rotp_kw.RotpKraftWorkAddon;
import com.danielgamer321.rotp_kw.capability.entity.EntityUtilCapProvider;
import com.danielgamer321.rotp_kw.capability.entity.ProjectileUtilCapProvider;
import com.danielgamer321.rotp_kw.entity.KWBlockEntity;
import com.danielgamer321.rotp_kw.entity.damaging.projectile.KWItemEntity;
import com.danielgamer321.rotp_kw.init.AddonStands;
import com.danielgamer321.rotp_kw.init.InitEffects;
import com.danielgamer321.rotp_kw.init.InitStands;
import com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType;
import com.github.standobyte.jojo.entity.itemprojectile.BladeHatEntity;
import com.github.standobyte.jojo.entity.itemprojectile.StandArrowEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;

import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SRemoveEntityEffectPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.List;
import java.util.Map;

import static com.danielgamer321.rotp_kw.action.stand.KraftWorkLockYourself.binding;

@EventBusSubscriber(modid = RotpKraftWorkAddon.MOD_ID)
public class GameplayEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        switch (event.phase) {
        case START:
            if (InitEffects.isLocked(player)) {
                player.setSprinting(false);
            }
            IStandPower.getStandPowerOptional(player).ifPresent(power -> {
                if (IStandPower.getStandPowerOptional(player).map(stand -> !stand.hasPower() ||
                        stand.getType() != AddonStands.KRAFT_WORK.getStandType()).orElse(false)) {
                    releaseFromLock(player);
                }
            });
            break;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void LockedPlayerInteraction(PlayerInteractEvent event) {
        if (event.isCancelable() && InitEffects.lockedArms(event.getPlayer())) {
            event.setCanceled(true);
            event.setCancellationResult(ActionResultType.FAIL);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void PlayerInteractionWithTheLockedEntity(PlayerInteractEvent.EntityInteractSpecific event) {
        Entity entity = event.getTarget();
        boolean lock = entity.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
        if (event.isCancelable() && lock) {
            event.setCanceled(true);
            event.setCancellationResult(ActionResultType.FAIL);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void PlayerInteractionWithTheLockedBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockPos eventPos = event.getHitVec().getBlockPos();
        List<KWBlockEntity> check = event.getWorld().getEntitiesOfClass(KWBlockEntity.class,
                new AxisAlignedBB(Vector3d.atCenterOf(eventPos), Vector3d.atCenterOf(eventPos)));
        if (event.isCancelable() && !check.isEmpty()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void ItemPickupInLock(EntityItemPickupEvent event) {
        if (InitEffects.shiftLocked(event.getPlayer()) || InitEffects.lockedArms(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void releaseLock(LivingConversionEvent.Post event) {
        if (event.getOutcome() instanceof MobEntity) {
            LivingEntity pre = event.getEntityLiving();
            MobEntity converted = (MobEntity) event.getOutcome();
            if (converted.isNoAi() && InitEffects.lockIA(pre) && !InitEffects.lockIA(converted)) {
                converted.setNoAi(false);
            }
        }
    }

//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public static void cancelBreakBlockByPlayer(BlockEvent.BreakEvent event) {
//        List<KWBlockEntity> check = event.getWorld().getEntitiesOfClass(KWBlockEntity.class,
//                new AxisAlignedBB(Vector3d.atCenterOf(event.getPos()), Vector3d.atCenterOf(event.getPos())));
//        if (!check.isEmpty()) {
//            event.setCanceled(true);
//        }
//    }

//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public static void cancelBreakBlockByEntites(LivingDestroyBlockEvent event) {
//        List<KWBlockEntity> check = event.getEntityLiving().level.getEntitiesOfClass(KWBlockEntity.class,
//                new AxisAlignedBB(Vector3d.atCenterOf(event.getPos()), Vector3d.atCenterOf(event.getPos())));
//        if (!check.isEmpty()) {
//            event.setCanceled(true);
//        }
//    }

//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public static void cancelBreakBlockByExplosion(ExplosionEvent.Detonate event) {
//        List<BlockPos> blocks = event.getExplosion().getToBlow();
//        if (!event.getAffectedBlocks().isEmpty()) {
//            for (BlockPos pos : blocks) {
//                List<KWBlockEntity> lockedBlocks = event.getWorld().getEntitiesOfClass(KWBlockEntity.class,
//                        new AxisAlignedBB(Vector3d.atCenterOf(pos), Vector3d.atCenterOf(pos)));
//                if (!lockedBlocks.isEmpty()) {
//                    for (KWBlockEntity lockedBlock : lockedBlocks) {
//                    }
//                }
//            }
//        }
//    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void lockedBlockInteract(BlockEvent.BlockToolInteractEvent event) {
        BlockPos eventPos = event.getPos();
        List<KWBlockEntity> check = event.getWorld().getEntitiesOfClass(KWBlockEntity.class,
                new AxisAlignedBB(Vector3d.atCenterOf(eventPos), Vector3d.atCenterOf(eventPos)));
        if (event.isCancelable() && !check.isEmpty()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player instanceof ServerPlayerEntity) {
            releaseFromLock((ServerPlayerEntity)player);
        }
    }

    private static void releaseFromLock(LivingEntity user) {
        String lock_id = String.valueOf(user.getUUID());
        MCUtil.getAllEntities(user.level).forEach(entity -> {
            if (entity.getTags().contains(lock_id)) {
                boolean positionLocking = entity.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
                if (positionLocking) {
                    if (entity instanceof LivingEntity && !(entity instanceof ArmorStandEntity)) {
                        LivingEntity lockedEntity = (LivingEntity) entity;
                        ItemStack helmet = lockedEntity.getItemBySlot(EquipmentSlotType.HEAD);
                        ItemStack chestplace = lockedEntity.getItemBySlot(EquipmentSlotType.CHEST);
                        ItemStack leggings = lockedEntity.getItemBySlot(EquipmentSlotType.LEGS);
                        ItemStack boots = lockedEntity.getItemBySlot(EquipmentSlotType.FEET);
                        lockedEntity.removeEffect(InitEffects.LOCKED_MAIN_HAND.get());
                        lockedEntity.removeEffect(InitEffects.LOCKED_OFF_HAND.get());
                        binding(user, true, helmet);
                        lockedEntity.removeEffect(InitEffects.LOCKED_HELMET.get());
                        binding(user, true, chestplace);
                        lockedEntity.removeEffect(InitEffects.LOCKED_CHESTPLATE.get());
                        binding(user, true, leggings);
                        lockedEntity.removeEffect(InitEffects.LOCKED_LEGGINGS.get());
                        binding(user, true, boots);
                        lockedEntity.removeEffect(InitEffects.LOCKED_POSITION.get());
                        lockedEntity.removeEffect(InitEffects.TRANSPORT_LOCKED.get());
                        lockedEntity.removeEffect(InitEffects.FULL_TRANSPORT_LOCKED.get());
                        KraftWorkStandType.setPositionLockingServerSide(entity, false);
                        KraftWorkStandType.TagServerSide(lockedEntity, lock_id, false);
                    }
                    else if (entity instanceof ProjectileEntity) {
                        ProjectileEntity projectile = (ProjectileEntity) entity;
                        int kineticEnergy = projectile.getCapability(ProjectileUtilCapProvider.CAPABILITY).map(cap -> cap.getKineticEnergy()).orElse(0);
                        Vector3d velocity = projectile instanceof FireworkRocketEntity ?
                                projectile.getDeltaMovement().normalize().add(user.level.random.nextGaussian() * (double)0.0075F * (double)0.0F, user.level.random.nextGaussian() * (double)0.0075F * (double)0.0F, user.level.random.nextGaussian() * (double)0.0075F * (double)0.0F).scale((double)3.15F + (0.143F * kineticEnergy)) :
                                projectile.getDeltaMovement().normalize().add(user.level.random.nextGaussian() * (double)0.0075F * (double)0.0F, user.level.random.nextGaussian() * (double)0.0075F * (double)0.0F, user.level.random.nextGaussian() * (double)0.0075F * (double)0.0F).scale((double)0.143F * kineticEnergy);
                        KraftWorkStandType.ReleaseProjectile(user, projectile, kineticEnergy, velocity);
                        projectile.setNoGravity(false);
                    }
                    else {
                        KraftWorkStandType.setPositionLockingServerSide(entity, false);
                        KraftWorkStandType.setCanUpdateServerSide(entity, true);
                        entity.setNoGravity(false);
                        KraftWorkStandType.TagServerSide(entity, lock_id, false);
                    }
                    if (entity.isVehicle()) {
                        for(Entity passengers : entity.getPassengers()) {
                            if (passengers instanceof LivingEntity) {
                                LivingEntity living = (LivingEntity) passengers;
                                living.removeEffect(InitEffects.TRANSPORT_LOCKED.get());
                            }
                        }
                    }
                }
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void handleDeath(LivingDeathEvent event) {
        if (!event.getEntity().level.isClientSide()) {
            LivingEntity dead = event.getEntityLiving();;
            IStandPower.getStandPowerOptional(dead).ifPresent(power -> {
                if (IStandPower.getStandPowerOptional(dead).map(stand -> stand.hasPower() &&
                        stand.getType() == AddonStands.KRAFT_WORK.getStandType()).orElse(false)) {
                    releaseFromLock(dead);
                }
            });
            projectileTotem(event);
        }
    }

    private static void projectileTotem(LivingDeathEvent event) {
        if (event.getSource().isProjectile()) {
            Entity entity = event.getSource().getDirectEntity();
            if (event.getSource().getDirectEntity() != null && entity instanceof KWItemEntity && ((KWItemEntity)entity).getItem().getItem() == Items.TOTEM_OF_UNDYING) {
                KWItemEntity projectile = (KWItemEntity) entity;
                LivingEntity dead = event.getEntityLiving();
                if (dead instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) dead;
                    serverplayerentity.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
                    CriteriaTriggers.USED_TOTEM.trigger(serverplayerentity, projectile.getItem());
                }
                event.setCanceled(true);
                dead.setHealth(1);
                dead.removeAllEffects();
                dead.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                dead.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                dead.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                dead.level.broadcastEntityEvent(dead, (byte)35);
                projectile.remove();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void BlockingItems(LivingHurtEvent event) {
        DamageSource dmgSource = event.getSource();
        LivingEntity target = event.getEntityLiving();
        if (dmgSource.getDirectEntity() != null) {
            Entity entity = dmgSource.getDirectEntity();
            if (dmgSource.isProjectile() || (isMelee(dmgSource) && entity instanceof LivingEntity &&
                    !((LivingEntity)entity).getMainHandItem().isEmpty())) {
                IStandPower.getStandPowerOptional(target).ifPresent(power -> {
                    if (IStandPower.getStandPowerOptional(target).map(stand ->
                            stand.hasPower() && stand.getType() == AddonStands.KRAFT_WORK.getStandType()).orElse(false) && ((KraftWorkStandType<?>) power.getType()).getStatus(power)
                            && InitStands.KRAFT_WORK_BI_STATUS.get().isUnlocked(power)) {
                        if (!dmgSource.isBypassArmor()) {
                            event.setAmount(((KraftWorkStandType<?>) power.getType()).KWReduceDamageAmount(
                                    power, power.getUser(), dmgSource, event.getAmount()));
                            power.consumeStamina(30F);
                        }
                        if (dmgSource.isProjectile()) {
                            if (entity instanceof StandArrowEntity || entity instanceof TridentEntity || entity instanceof BladeHatEntity ||
                                    (entity instanceof KWItemEntity && !((KWItemEntity)entity).willBeRemovedOnEntityHit())) {
                                String lock_id = String.valueOf(target.getUUID());
                                KraftWorkStandType.setPositionLockingServerSide(entity, true);
                                KraftWorkStandType.TagServerSide(entity, lock_id, true);
                                entity.getCapability(ProjectileUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.setKineticEnergy(0));
                            }
                        }
                        else if (isMelee(dmgSource) && entity instanceof LivingEntity && !(entity instanceof StandEntity)) {
                            String lock_id = String.valueOf(target.getUUID());
                            LivingEntity living = (LivingEntity) entity;
                            living.addEffect(new EffectInstance(InitEffects.LOCKED_MAIN_HAND.get(), 19999980, 0, false, false, true));
                            KraftWorkStandType.setPositionLockingServerSide(entity, true);
                            KraftWorkStandType.TagServerSide(entity, lock_id, true);
                        }
                        power.addLearningProgressPoints(InitStands.KRAFT_WORK_BI_STATUS.get(), 0.001F);
                    }
                });
            }
        }
    }

    private static boolean isMelee(DamageSource dmgSource) {
        return !dmgSource.isExplosion() && !dmgSource.isFire() && !dmgSource.isMagic() &&
                !dmgSource.isProjectile();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPotionAdded(PotionEvent.PotionAddedEvent event) {
        EntityStandType.giveEffectSharedWithStand(event.getEntityLiving(), event.getPotionEffect());

        Entity entity = event.getEntity();
        EffectInstance effectInstance = event.getPotionEffect();
        if (!entity.level.isClientSide()) {
            if (InitEffects.isEffectTracked(effectInstance.getEffect())) {
                ((ServerChunkProvider) entity.getCommandSenderWorld().getChunkSource()).broadcast(entity,
                        new SPlayEntityEffectPacket(entity.getId(), effectInstance));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void trackedPotionRemoved(PotionEvent.PotionRemoveEvent event) {
        EntityStandType.removeEffectSharedWithStand(event.getEntityLiving(), event.getPotion());

        Entity entity = event.getEntity();
        if (!entity.level.isClientSide() && event.getPotionEffect() != null && InitEffects.isEffectTracked(event.getPotionEffect().getEffect())) {
            ((ServerChunkProvider) entity.getCommandSenderWorld().getChunkSource()).broadcast(entity,
                    new SRemoveEntityEffectPacket(entity.getId(), event.getPotion()));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void trackedPotionExpired(PotionEvent.PotionExpiryEvent event) {
        EntityStandType.removeEffectSharedWithStand(event.getEntityLiving(), event.getPotionEffect().getEffect());

        Entity entity = event.getEntity();
        if (!entity.level.isClientSide() && InitEffects.isEffectTracked(event.getPotionEffect().getEffect())) {
            ((ServerChunkProvider) entity.getCommandSenderWorld().getChunkSource()).broadcast(entity,
                    new SRemoveEntityEffectPacket(entity.getId(), event.getPotionEffect().getEffect()));
        }
    }

    @SubscribeEvent
    public static void syncTrackedEffects(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            LivingEntity tracked = (LivingEntity) event.getTarget();
            for (Map.Entry<Effect, EffectInstance> effectEntry : tracked.getActiveEffectsMap().entrySet()) {
                if (InitEffects.isEffectTracked(effectEntry.getKey())) {
                    player.connection.send(new SPlayEntityEffectPacket(tracked.getId(), effectEntry.getValue()));
                }
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGameModeChange(PlayerEvent.PlayerChangeGameModeEvent event) {
        if (event.getNewGameMode() == GameType.CREATIVE) {
            PlayerEntity player = event.getPlayer();
            player.removeEffect(InitEffects.LOCKED_MAIN_HAND.get());
            player.removeEffect(InitEffects.LOCKED_OFF_HAND.get());
            player.removeEffect(InitEffects.LOCKED_HELMET.get());
            player.removeEffect(InitEffects.LOCKED_CHESTPLATE.get());
            player.removeEffect(InitEffects.LOCKED_LEGGINGS.get());
            player.removeEffect(InitEffects.LOCKED_POSITION.get());
            player.removeEffect(InitEffects.TRANSPORT_LOCKED.get());
            player.removeEffect(InitEffects.FULL_TRANSPORT_LOCKED.get());
        }
    }
}

package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.entity.damaging.projectile.KWItemEntity;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.entity.damaging.projectile.TommyGunBulletEntity;
import com.github.standobyte.jojo.entity.itemprojectile.BladeHatEntity;
import com.github.standobyte.jojo.entity.itemprojectile.KnifeEntity;
import com.github.standobyte.jojo.init.ModItems;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.item.KnifeItem;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.LazySupplier;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.item.ExperienceBottleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.stream.Stream;

import static com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType.*;

public class KraftWorkPlaceProjectile extends StandAction {
    public KraftWorkPlaceProjectile(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (user.hasEffect(ModStatusEffects.IMMOBILIZE.get())) {
            return ActionConditionResult.NEGATIVE;
        }
        ItemStack itemToShoot = user.getOffhandItem();
        if (itemToShoot == null || itemToShoot.isEmpty()) {
            return conditionMessage("item_offhand");
        }
        if (uselessItems(itemToShoot.getItem())) {
            return conditionMessage("useless_item");
        }
        PlayerEntity player = (PlayerEntity) user;
        if (player.getCooldowns().isOnCooldown(itemToShoot.getItem())) {
            return conditionMessage("item_cooldown");
        }
        return super.checkSpecificConditions(user, power, target);
    }

    private boolean projectileList(ItemStack item) {
        return
                item.getItem() instanceof ArrowItem ||
                item.getItem() instanceof ThrowablePotionItem ||
                item.getItem() instanceof KnifeItem ||
                item.getItem() == ModItems.BLADE_HAT.get() ||
                item.getItem() == Items.TRIDENT ||
                item.getItem() == Items.IRON_NUGGET ||
                item.getItem() == Items.FIRE_CHARGE ||
                item.getItem() == Items.FIREWORK_ROCKET ||
                item.getItem() == Items.ENDER_PEARL ||
                item.getItem() == Items.SNOWBALL ||
                item.getItem() == Items.EGG ||
                item.getItem() == Items.EXPERIENCE_BOTTLE;
    }

    private boolean ineffectiveItems(Item item) {
        return
                item == Items.OAK_SAPLING ||
                item == Items.SPRUCE_SAPLING ||
                item == Items.BIRCH_SAPLING ||
                item == Items.JUNGLE_SAPLING ||
                item == Items.ACACIA_SAPLING ||
                item == Items.DARK_OAK_SAPLING ||
                item == Items.APPLE ||
                item == Items.GOLDEN_APPLE ||
                item == Items.ENCHANTED_GOLDEN_APPLE ||
                item == Items.MELON_SLICE ||
                item == Items.GLISTERING_MELON_SLICE ||
                item == Items.GOLDEN_CARROT ||
                item == Items.POTATO ||
                item == Items.BAKED_POTATO ||
                item == Items.POISONOUS_POTATO ||
                item == Items.BOOK ||
                item == Items.WRITABLE_BOOK ||
                item == Items.WRITTEN_BOOK ||
                item == Items.ENCHANTED_BOOK ||
                item == Items.KNOWLEDGE_BOOK ||
                item == Items.SCUTE ||
                item == Items.TURTLE_HELMET ||
                item == Items.NAUTILUS_SHELL ||
                item == Items.SHULKER_SHELL ||
                item == Items.DRAGON_EGG;
    }

    private boolean weakItems(Item item) {
        return
                item == Items.RABBIT_HIDE ||
                item == Items.RABBIT_FOOT ||
                item == Items.LEATHER ||
                item == Items.INK_SAC ||
                item == Items.BEETROOT_SEEDS ||
                item == Items.MELON_SEEDS ||
                item == Items.PUMPKIN_SEEDS ||
                item == Items.WHEAT_SEEDS ||
                item == Items.WHEAT ||
                item == Items.KELP ||
                item == Items.DRIED_KELP ||
                item == Items.FEATHER ||
                item == Items.STRING ||
                item == Items.COBWEB ||
                item == Items.PAPER ||
                item == Items.MAP ||
                item == Items.CLAY_BALL ||
                item == Items.FIREWORK_STAR ||
                (item.isEdible() &&
                 !(item == Items.BEETROOT_SOUP ||
                 item == Items.MUSHROOM_STEW ||
                 item == Items.RABBIT_STEW ||
                 item == Items.SUSPICIOUS_STEW)) ||
                item == Items.SLIME_BALL ||
                item == Items.NETHER_WART ||
                item == Items.GHAST_TEAR ||
                item == Items.MAGMA_CREAM ||
                item == Items.GLASS_BOTTLE ||
                item == Items.TURTLE_EGG ||
                item == Items.PHANTOM_MEMBRANE ||
                item instanceof BannerPatternItem ||
                item instanceof DyeableArmorItem ||
                item instanceof PotionItem ||
                item instanceof SpawnEggItem;
    }

    private boolean uselessItems(Item item) {
        return
                item instanceof DyeItem ||
                item == Items.BONE_MEAL ||
                item == Items.GUNPOWDER ||
                item == Items.SUGAR ||
                item == Items.REDSTONE ||
                item == Items.GLOWSTONE_DUST ||
                item == Items.BLAZE_POWDER;
    }

    private boolean hardMaterial(BlockState blockState) {
        Material material = blockState.getMaterial();
        return
                material == Material.BUILDABLE_GLASS ||
                material == Material.ICE_SOLID ||
                material == Material.WOOD ||
                material == Material.NETHER_WOOD ||
                material == Material.GLASS ||
                material == Material.ICE ||
                material == Material.STONE ||
                material == Material.METAL ||
                material == Material.HEAVY_METAL ||
                material == Material.CLAY && blockState.getBlock().getRegistryName().getPath().contains("infested");
    }

    private boolean ineffectiveMaterial(BlockState blockState) {
        Material material = blockState.getMaterial();
        return
                material == Material.DECORATION ||
                material == Material.SHULKER_SHELL ||
                material == Material.BAMBOO ||
                material == Material.CACTUS ||
                material == Material.CORAL ||
                material == Material.VEGETABLE;
    }

    private boolean weakMaterial(BlockState blockState) {
        Material material = blockState.getMaterial();
        return
                material == Material.CLOTH_DECORATION ||
                material == Material.PLANT ||
                material == Material.WATER_PLANT ||
                material == Material.REPLACEABLE_PLANT ||
                material == Material.REPLACEABLE_FIREPROOF_PLANT ||
                material == Material.REPLACEABLE_WATER_PLANT ||
                material == Material.TOP_SNOW  ||
                material == Material.WEB ||
                material == Material.CLAY ||
                material == Material.DIRT ||
                material == Material.GRASS ||
                material == Material.SAND ||
                material == Material.SPONGE ||
                material == Material.WOOL ||
                material == Material.EXPLOSIVE ||
                material == Material.LEAVES ||
                material == Material.CAKE;
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide()) {
            ItemStack item = user.getOffhandItem();
            Vector3d userView = user.getViewVector(1.0F);
            int itemsToThrow = 1;
            PlayerEntity player = (PlayerEntity) user;
            String lock_id = String.valueOf(user.getUUID());
            if (projectileList(item)) {
                Item proItem = item.getItem();
                if (proItem instanceof ArrowItem) {
                    ArrowItem arrowItem = (ArrowItem) proItem;
                    AbstractArrowEntity arrow = arrowItem.createArrow(world, item, user);
                    arrow.setPos(arrow.getX() + userView.x * 0.75D, user.getY(0.5D) + 0.5D, arrow.getZ() + userView.z * 0.75D);
                    arrow.shootFromRotation(user, user.xRot, user.yRot, 0.0F, 1.5F, 1.0F);
                    arrow.setBaseDamage(arrow.getBaseDamage() / 6);
                    TagServerSide(arrow, lock_id, true);
                    if (player.abilities.instabuild) {
                        arrow.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                    }
                    world.addFreshEntity(arrow);
                    setCanUpdateServerSide(arrow, false);
                    setPositionLockingServerSide(arrow, true);
                    player.getCooldowns().addCooldown(proItem, 3);
                }
                if (proItem instanceof ThrowablePotionItem) {
                    PotionEntity potion = new PotionEntity(world, user);
                    potion.setItem(item);
                    potion.setPos(potion.getX() + userView.x * 0.5D, user.getY(0.5D) + 0.5D, potion.getZ() + userView.z * 0.5D);
                    potion.shootFromRotation(user, user.xRot, user.yRot, 0.0F, 1.5F, 1.0F);
                    TagServerSide(potion, lock_id, true);
                    world.addFreshEntity(potion);
                    setCanUpdateServerSide(potion, false);
                    setPositionLockingServerSide(potion, true);
                    player.getCooldowns().addCooldown(proItem, 3);
                }
                if (proItem instanceof KnifeItem) {
                    KnifeItem knifeItem = (KnifeItem) proItem;
                    if (power.getResolveLevel() >= 3) {
                        itemsToThrow = !user.isShiftKeyDown() ? Math.min(item.getCount(), knifeItem.MAX_KNIVES_THROW) : 1;
                    }

                    for (int i = 0; i < itemsToThrow; i++) {
                        int flightTicks = 0;
                        if (!user.isShiftKeyDown() && power.getResolveLevel() >= 3) {
                            flightTicks = 1;
                        }
                        KnifeEntity knife = new KnifeEntity(world, user);
                        if (flightTicks == 0) {
                            knife.setPos(knife.getX() + userView.x * 0.5D, user.getY(0.5D) + 0.5D, knife.getZ() + userView.z * 0.5D);
                        }
                        knife.setTimeStopFlightTicks(flightTicks);
                        knife.shootFromRotation(user, 1.5F, i == 0 ? 1.0F : 16.0F);
                        knife.setBaseDamage(knife.getBaseDamage() / 6);
                        TagServerSide(knife, lock_id, true);
                        world.addFreshEntity(knife);
                        setCanUpdateServerSide(knife, false);
                        setPositionLockingServerSide(knife, true);
                    }

                    if (power.getResolveLevel() >= 3 && !user.isShiftKeyDown()) {
                        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                                itemsToThrow == 1 ? ModSounds.KNIFE_THROW.get() : ModSounds.KNIVES_THROW.get(),
                                SoundCategory.PLAYERS, 0 * 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
                    }
                    int cooldown = itemsToThrow * 3;
                    player.getCooldowns().addCooldown(proItem, cooldown);
                }
                if (proItem == ModItems.BLADE_HAT.get()) {
                    BladeHatEntity hat = new BladeHatEntity(world, user, item);
                    hat.setPos(hat.getX(), user.getY(0.5D) + 0.5D, hat.getZ() + userView.z * 0.75D);
                    hat.shootFromRotation(user, 1.5F, 0.5F);
                    hat.setBaseDamage(hat.getBaseDamage() / 6);
                    TagServerSide(hat, lock_id, true);
                    world.addFreshEntity(hat);
                    setCanUpdateServerSide(hat, false);
                    setPositionLockingServerSide(hat, true);
                    player.getCooldowns().addCooldown(proItem, 3);
                }
                if (proItem == Items.TRIDENT) {
                    TridentItem tridentItem = (TridentItem) proItem;
                    int i = tridentItem.getUseDuration(item) - user.getUseItemRemainingTicks();
                    if (i >= 10) {
                        int j = EnchantmentHelper.getRiptide(item);
                        if (j <= 0 || player.isInWaterOrRain()) {
                            item.hurtAndBreak(1, player, (PUser) -> {
                                PUser.broadcastBreakEvent(player.getUsedItemHand());
                            });
                            TridentEntity trident = new TridentEntity(world, player, item);
                            trident.setPos(trident.getX(), user.getY(0.5D) + 0.5D, trident.getZ() + userView.z * 0.75D);
                            trident.shootFromRotation(user, user.xRot, user.yRot, 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
                            trident.setBaseDamage(trident.getBaseDamage() / 6);
                            TagServerSide(trident, lock_id, true);
                            if (player.abilities.instabuild) {
                                trident.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                            }
                            world.addFreshEntity(trident);
                            setCanUpdateServerSide(trident, false);
                            setPositionLockingServerSide(trident, true);
                            player.getCooldowns().addCooldown(proItem, 5);
                            player.awardStat(Stats.ITEM_USED.get(proItem));
                        }
                    }
                }
                if (proItem == Items.IRON_NUGGET) {
                    TommyGunBulletEntity bullet = new TommyGunBulletEntity(user, world);
                    bullet.setPos(bullet.getX() + userView.x * 0.5D, user.getY(0.5D) + 0.5D, bullet.getZ() + userView.z * 0.5D);
                    bullet.shootFromRotation(user, 1.5F, 0.0F);
                    TagServerSide(bullet, lock_id, true);
                    world.addFreshEntity(bullet);
                    setCanUpdateServerSide(bullet, false);
                    setPositionLockingServerSide(bullet, true);
                    player.getCooldowns().addCooldown(proItem, 3);
                }
                if (proItem == Items.FIRE_CHARGE) {
                    double d0 = user.getX() + (userView.x * 0.3F);
                    double d1 = user.getY() + (userView.y * 0.3F);
                    double d2 = user.getZ() + (userView.z * 0.3F);
                    double d3 = userView.x;
                    double d4 = userView.y;
                    double d5 = userView.z;
                    SmallFireballEntity fireBall = new SmallFireballEntity(world, d0, d1, d2, d3, d4, d5);
                    fireBall.setPos(fireBall.getX() + userView.x * 0.5D, user.getY(0.5D) + 0.5D, fireBall.getZ() + userView.z * 0.5D);
                    fireBall.shootFromRotation(user, user.xRot, user.yRot, 0.0F, 1.5F, 1.0F);
                    fireBall.setOwner(user);
                    TagServerSide(fireBall, lock_id, true);
                    world.addFreshEntity(fireBall);
                    setCanUpdateServerSide(fireBall, false);
                    setPositionLockingServerSide(fireBall, true);
                    player.getCooldowns().addCooldown(proItem, 10);
                }
                if (proItem == Items.FIREWORK_ROCKET) {
                    FireworkRocketEntity firework = new FireworkRocketEntity(world, item, user, user.getX(), user.getEyeY() - (double)0.15F, user.getZ(), true);
                    firework.setPos(firework.getX() + userView.x * 0.5D, user.getY(0.5D) + 0.5D, firework.getZ() + userView.z * 0.5D);
                    firework.shootFromRotation(user, user.xRot, user.yRot, 0.0F, 1.5F, 1.0F);
                    TagServerSide(firework, lock_id, true);
                    world.addFreshEntity(firework);
                    setCanUpdateServerSide(firework, false);
                    setPositionLockingServerSide(firework, true);
                    player.getCooldowns().addCooldown(proItem, 10);
                }
                if (proItem == Items.ENDER_PEARL) {
                    EnderPearlEntity enderPearl = new EnderPearlEntity(world, user);
                    enderPearl.setItem(item);
                    enderPearl.setPos(enderPearl.getX() + userView.x * 0.5D, user.getY(0.5D) + 0.5D, enderPearl.getZ() + userView.z * 0.5D);
                    enderPearl.shootFromRotation(user, user.xRot, user.yRot, 0.0F, 1.5F, 1.0F);
                    TagServerSide(enderPearl, lock_id, true);
                    world.addFreshEntity(enderPearl);
                    setCanUpdateServerSide(enderPearl, false);
                    setPositionLockingServerSide(enderPearl, true);
                    player.getCooldowns().addCooldown(proItem, 20);
                    player.awardStat(Stats.ITEM_USED.get(proItem));
                }
                if (proItem == Items.SNOWBALL) {
                    SnowballEntity snowBall = new SnowballEntity(world, user);
                    snowBall.setItem(item);
                    snowBall.setPos(snowBall.getX() + userView.x * 0.5D, user.getY(0.5D) + 0.5D, snowBall.getZ() + userView.z * 0.5D);
                    snowBall.shootFromRotation(user, user.xRot, user.yRot, 0.0F, 1.5F, 1.0F);
                    TagServerSide(snowBall, lock_id, true);
                    world.addFreshEntity(snowBall);
                    setCanUpdateServerSide(snowBall, false);
                    setPositionLockingServerSide(snowBall, true);
                    player.getCooldowns().addCooldown(proItem, 3);
                    player.awardStat(Stats.ITEM_USED.get(proItem));
                }
                if (proItem == Items.EGG) {
                    EggEntity egg = new EggEntity(world, user);
                    egg.setItem(item);
                    egg.setPos(egg.getX() + userView.x * 0.5D, user.getY(0.5D) + 0.5D, egg.getZ() + userView.z * 0.5D);
                    egg.shootFromRotation(user, user.xRot, user.yRot, 0.0F, 0.75F, 1.0F);
                    egg.setOwner(user);
                    TagServerSide(egg, lock_id, true);
                    world.addFreshEntity(egg);
                    setCanUpdateServerSide(egg, false);
                    setPositionLockingServerSide(egg, true);
                    player.getCooldowns().addCooldown(proItem, 3);
                    player.awardStat(Stats.ITEM_USED.get(proItem));
                }
                if (proItem == Items.EXPERIENCE_BOTTLE) {
                    ExperienceBottleEntity exp = new ExperienceBottleEntity(world, user);
                    exp.setItem(item);
                    exp.setPos(exp.getX() + userView.x * 0.5D, user.getY(0.5D) + 0.5D, exp.getZ() + userView.z * 0.5D);
                    exp.shootFromRotation(user, user.xRot, user.yRot, 0.0F, 0.75F, 1.0F);
                    exp.setOwner(user);
                    TagServerSide(exp, lock_id, true);
                    world.addFreshEntity(exp);
                    setCanUpdateServerSide(exp, false);
                    setPositionLockingServerSide(exp, true);
                    player.getCooldowns().addCooldown(proItem, 3);
                }
            }
            else if (item.getItem() != Items.ENDER_EYE) {
                Item proItem = item.getItem();
                KWItemEntity projectileItem = new KWItemEntity(user, world);
                projectileItem.setItem(item);
                if (!(item.getItem() instanceof BlockItem)) {
                    if (ineffectiveItems(proItem)) {
                        projectileItem.setBaseDamage(projectileItem.getBaseDamage() / 2);
                    }
                    if (weakItems(proItem) && !ineffectiveItems(proItem)) {
                        projectileItem.setBaseDamage(0);
                    }
                }
                else {
                    Block block = ((BlockItem) proItem).getBlock();
                    BlockState blockState = block.defaultBlockState();
                    if (ineffectiveMaterial(blockState) || ineffectiveItems(proItem)) {
                        projectileItem.setBaseDamage(projectileItem.getBaseDamage() / 2);
                    }
                    if (weakMaterial(blockState) && !hardMaterial(blockState) &&
                            !ineffectiveMaterial(blockState) && !ineffectiveItems(proItem)) {
                        projectileItem.setBaseDamage(0);
                    }
                }
                if (proItem == Items.SLIME_BLOCK ||
                    proItem == Items.TOTEM_OF_UNDYING ||
                    proItem == Items.ENCHANTED_GOLDEN_APPLE ||
                    proItem == Items.NETHER_STAR ||
                    proItem == Items.NETHERITE_INGOT ||
                    proItem == Items.NETHERITE_SCRAP ||
                    proItem == Items.SHULKER_BOX ||
                    proItem == Items.BEACON ||
                    proItem == Items.DRAGON_EGG ||
                    proItem instanceof TieredItem ||
                    proItem instanceof ShootableItem ||
                    proItem instanceof ArmorItem ||
                    proItem instanceof HorseArmorItem ||
                    (proItem.getUseDuration(item) > 0 &&
                     !(proItem instanceof PotionItem) &&
                     !proItem.isEdible())) {
                    projectileItem.setRemoveItem(false);
                    if (proItem instanceof ToolItem && !(proItem instanceof HoeItem)) {
                        ToolItem tool = (ToolItem) proItem;
                        projectileItem.setBaseDamage(tool.getAttackDamage());
                        if (proItem instanceof AxeItem) {
                            projectileItem.setBaseDamage(projectileItem.getBaseDamage() - 1);
                        }
                    }
                    else if (proItem instanceof SwordItem) {
                        SwordItem sword = (SwordItem) proItem;
                        projectileItem.setBaseDamage(sword.getDamage() - 1);
                    }
                }
                if (proItem instanceof PotionItem ||
                    proItem == Items.TORCH ||
                    proItem == Items.SOUL_TORCH ||
                    proItem == Items.REDSTONE_TORCH ||
                    proItem == Items.CAMPFIRE ||
                    proItem == Items.SOUL_CAMPFIRE ||
                    proItem == Items.GLASS ||
                    proItem == Items.GLASS_PANE ||
                    proItem == Items.GLASS_BOTTLE ||
                    proItem == Items.PRISMARINE_CRYSTALS ||
                    proItem == Items.SEA_LANTERN ||
                    proItem == Items.MILK_BUCKET ||
                    proItem == Items.BEE_NEST ||
                    proItem == Items.END_CRYSTAL ||
                    proItem instanceof BucketItem ||
                    proItem instanceof SpawnEggItem) {
                    projectileItem.removeAfterHitting(true);
                }
                if (proItem == Items.TOTEM_OF_UNDYING) {
                    projectileItem.setBeneficial(true);
                }
                projectileItem.setPos(projectileItem.getX() + userView.x * 0.5D, user.getY(0.5D) + 0.5D, projectileItem.getZ() + userView.z * 0.5D);
                projectileItem.shootFromRotation(user, user.xRot, user.yRot, 0.0F, 1.5F, 1.0F);
                projectileItem.setBaseDamage(projectileItem.getBaseDamage() / 6);
                TagServerSide(projectileItem, lock_id, true);
                world.addFreshEntity(projectileItem);
                setCanUpdateServerSide(projectileItem, false);
                setPositionLockingServerSide(projectileItem, true);
                player.getCooldowns().addCooldown(proItem, 3);
            }
            else {
                if (world instanceof ServerWorld) {
                    BlockPos blockpos = ((ServerWorld)world).getChunkSource().getGenerator().findNearestMapFeature((ServerWorld)world, Structure.STRONGHOLD, user.blockPosition(), 100, false);
                    if (blockpos != null) {
                        EyeOfEnderEntity eyeOfEnder = new EyeOfEnderEntity(world, user.getX(), user.getY(0.5D), user.getZ());
                        eyeOfEnder.setItem(item);
                        eyeOfEnder.signalTo(blockpos);
                        TagServerSide(eyeOfEnder, lock_id, true);
                        world.addFreshEntity(eyeOfEnder);
                        setCanUpdateServerSide(eyeOfEnder, false);
                        setPositionLockingServerSide(eyeOfEnder, true);
                        if (player instanceof ServerPlayerEntity) {
                            CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayerEntity)player, blockpos);
                        }
                        world.levelEvent((PlayerEntity)null, 1003, user.blockPosition(), 0);
                        player.awardStat(Stats.ITEM_USED.get(item.getItem()));
                        player.getCooldowns().addCooldown(item.getItem(), 3);
                    }
                }
            }
            if (!player.abilities.instabuild) {
                item.shrink(itemsToThrow);
            }
        }
    }

    @Override
    public float getStaminaCost(IStandPower stand) {
        LivingEntity user = stand.getUser();
        ItemStack item = user.getOffhandItem();
        if (item.getItem() instanceof KnifeItem && stand.getResolveLevel() >= 3) {
            int itemsToThrow = !user.isShiftKeyDown() ? Math.min(item.getCount(), 8) : 1;
            return 10 * itemsToThrow;
        }
        return super.getStaminaCost(stand);
    }

    @Override
    public String getTranslationKey(IStandPower power, ActionTarget target) {
        String key = super.getTranslationKey(power, target);
        if (isNotAProjectile(power)) {
            key += ".item";
        }
        return key;
    }

    private final LazySupplier<ResourceLocation> itemTex =
            new LazySupplier<>(() -> makeIconVariant(this, "_item"));
    @Override
    public ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        if (power != null && isNotAProjectile(power)) {
            return itemTex.get();
        }
        else {
            return super.getIconTexturePath(power);
        }
    }

    private boolean isNotAProjectile(IStandPower power) {
        ItemStack item = power.getUser().getOffhandItem();
        return power.getUser() != null && !projectileList(item);
    }



    @Deprecated
    private ResourceLocation itemTexPath;
    @Deprecated
    @Override
    public ResourceLocation getTexture(IStandPower power) {
        ResourceLocation resLoc = getRegistryName();
        if (isNotAProjectile(power)) {
            if (itemTexPath == null) {
                itemTexPath = new ResourceLocation(resLoc.getNamespace(), resLoc.getPath() + "_item");
            }
            resLoc = itemTexPath;
        }
        return resLoc;
    }

    @Deprecated
    @Override
    public Stream<ResourceLocation> getTexLocationstoLoad() {
        ResourceLocation resLoc = getRegistryName();
        return Stream.of(resLoc, new ResourceLocation(resLoc.getNamespace(), resLoc.getPath() + "_item"));
    }
}

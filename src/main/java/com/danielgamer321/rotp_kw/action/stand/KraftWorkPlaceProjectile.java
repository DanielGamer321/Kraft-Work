package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.entity.damaging.projectile.KWItemEntity;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.entity.damaging.projectile.MolotovEntity;
import com.github.standobyte.jojo.entity.damaging.projectile.TommyGunBulletEntity;
import com.github.standobyte.jojo.entity.itemprojectile.BladeHatEntity;
import com.github.standobyte.jojo.entity.itemprojectile.KnifeEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
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
import net.minecraft.enchantment.Enchantments;
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
        if (!(user instanceof PlayerEntity)) {
            return ActionConditionResult.NEGATIVE;
        }
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

    public static boolean projectileList(ItemStack item, LivingEntity user) {
        ItemStack mainItem = user.getMainHandItem();
        return
                item.getItem() instanceof ArrowItem ||
                item.getItem() instanceof ThrowablePotionItem ||
                item.getItem() instanceof KnifeItem ||
                (item.getItem() == ModItems.MOLOTOV.get() && mainItem.getItem() == Items.FLINT_AND_STEEL) ||
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

    public static boolean ineffectiveItems(Item item) {
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

    public static boolean weakItems(Item item) {
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

    public static boolean uselessItems(Item item) {
        return
                item instanceof DyeItem ||
                item == Items.BONE_MEAL ||
                item == Items.GUNPOWDER ||
                item == Items.SUGAR ||
                item == Items.REDSTONE ||
                item == Items.GLOWSTONE_DUST ||
                item == Items.BLAZE_POWDER;
    }

    public static boolean hardMaterial(BlockState blockState) {
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

    public static boolean ineffectiveMaterial(BlockState blockState) {
        Material material = blockState.getMaterial();
        return
                material == Material.DECORATION ||
                material == Material.SHULKER_SHELL ||
                material == Material.BAMBOO ||
                material == Material.CACTUS ||
                material == Material.CORAL ||
                material == Material.VEGETABLE;
    }

    public static boolean weakMaterial(BlockState blockState) {
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

    public static boolean doNotRemove(Item proItem, ItemStack item) {
        return
                proItem == Items.SLIME_BLOCK || proItem == Items.TOTEM_OF_UNDYING ||
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
                !proItem.isEdible());
    }

    public static boolean removeOnImpact(Item item){
        return
                item instanceof PotionItem ||
                item == Items.TORCH ||
                item == Items.SOUL_TORCH ||
                item == Items.REDSTONE_TORCH ||
                item == Items.CAMPFIRE ||
                item == Items.SOUL_CAMPFIRE ||
                item == Items.GLASS ||
                item == Items.GLASS_PANE ||
                item == Items.GLASS_BOTTLE ||
                item == Items.PRISMARINE_CRYSTALS ||
                item == Items.SEA_LANTERN ||
                item == Items.MILK_BUCKET ||
                item == Items.BEE_NEST ||
                item == Items.END_CRYSTAL ||
                item instanceof BucketItem ||
                item instanceof SpawnEggItem;
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide()) {
            ItemStack item = user.getOffhandItem();
            placeProjectile(world, user, user, item, power);
        }
    }

    public static void placeProjectile(World world, LivingEntity user, LivingEntity itemOwner, ItemStack item, IStandPower power) {
        Vector3d userView = itemOwner.getViewVector(1.0F);
        ItemStack mainItem = user.getMainHandItem();
        int itemsToThrow = 1;
        String lock_id = String.valueOf(user.getUUID());
        if (projectileList(item, itemOwner)) {
            Item proItem = item.getItem();
            if (proItem instanceof ArrowItem) {
                ArrowItem arrowItem = (ArrowItem) proItem;
                AbstractArrowEntity arrow = arrowItem.createArrow(world, item, itemOwner);
                arrow.setPos(arrow.getX() + userView.x * 0.75D, itemOwner.getY(0.5D) + 0.5D, arrow.getZ() + userView.z * 0.75D);
                arrow.shootFromRotation(itemOwner, itemOwner.xRot, itemOwner.yRot, 0.0F, 1.5F, 1.0F);
                arrow.setBaseDamage(arrow.getBaseDamage() / 6);
                arrow.setOwner(user);
                TagServerSide(arrow, lock_id, true);
                if (itemOwner instanceof PlayerEntity) {
                    if (((PlayerEntity)itemOwner).abilities.instabuild) {
                        arrow.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                    }
                    ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, 3);
                }
                world.addFreshEntity(arrow);
                setCanUpdateServerSide(arrow, false);
                setPositionLockingServerSide(arrow, true);
            }
            if (proItem instanceof ThrowablePotionItem) {
                PotionEntity potion = new PotionEntity(world, itemOwner);
                potion.setItem(item);
                potion.setPos(potion.getX() + userView.x * 0.5D, itemOwner.getY(0.5D) + 0.5D, potion.getZ() + userView.z * 0.5D);
                potion.shootFromRotation(itemOwner, itemOwner.xRot, itemOwner.yRot, 0.0F, 1.5F, 1.0F);
                potion.setOwner(user);
                TagServerSide(potion, lock_id, true);
                world.addFreshEntity(potion);
                setCanUpdateServerSide(potion, false);
                setPositionLockingServerSide(potion, true);
                if (itemOwner instanceof PlayerEntity) {
                    ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, 3);
                }
            }
            if (proItem instanceof KnifeItem) {
                KnifeItem knifeItem = (KnifeItem) proItem;
                if (power.getResolveLevel() >= 3) {
                    itemsToThrow = !itemOwner.isShiftKeyDown() ? Math.min(item.getCount(), knifeItem.MAX_KNIVES_THROW) : 1;
                }

                for (int i = 0; i < itemsToThrow; i++) {
                    int flightTicks = 0;
                    if (!itemOwner.isShiftKeyDown() && power.getResolveLevel() >= 3 && !(itemOwner instanceof StandEntity)) {
                        flightTicks = 1;
                    }
                    KnifeEntity knife = new KnifeEntity(world, itemOwner);
                    if (flightTicks == 0) {
                        knife.setPos(knife.getX() + userView.x * 0.5D, itemOwner.getY(0.5D) + 0.5D, knife.getZ() + userView.z * 0.5D);
                    }
                    knife.setTimeStopFlightTicks(flightTicks);
                    knife.shootFromRotation(itemOwner, 1.5F, i == 0 ? 1.0F : 16.0F);
                    knife.setBaseDamage(knife.getBaseDamage() / 6);
                    knife.setOwner(user);
                    TagServerSide(knife, lock_id, true);
                    world.addFreshEntity(knife);
                    setCanUpdateServerSide(knife, false);
                    setPositionLockingServerSide(knife, true);
                }

                if (power.getResolveLevel() >= 3 && !itemOwner.isShiftKeyDown() && !(itemOwner instanceof StandEntity)) {
                    world.playSound(null, user.getX(), user.getY(), user.getZ(),
                            itemsToThrow == 1 ? ModSounds.KNIFE_THROW.get() : ModSounds.KNIVES_THROW.get(),
                            SoundCategory.PLAYERS, 0 * 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
                }
                int cooldown = itemsToThrow * 3;
                if (itemOwner instanceof PlayerEntity) {
                    ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, cooldown);
                }
            }
            if (proItem == ModItems.MOLOTOV.get() && mainItem.getItem() == Items.FLINT_AND_STEEL) {
                MolotovEntity molotov = new MolotovEntity(world, itemOwner);
                molotov.setItem(item);
                molotov.shootFromRotation(itemOwner, itemOwner.xRot, itemOwner.yRot, 0.0F, 0.75F, 1.0F);
                TagServerSide(molotov, lock_id, true);
                world.addFreshEntity(molotov);
                setCanUpdateServerSide(molotov, false);
                setPositionLockingServerSide(molotov, true);
                if (itemOwner instanceof PlayerEntity) {
                    ((PlayerEntity)itemOwner).awardStat(Stats.ITEM_USED.get(proItem));
                    ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, 3);
                }
                mainItem.hurtAndBreak(1, itemOwner, (PUser) -> {
                    PUser.broadcastBreakEvent(itemOwner.getUsedItemHand());
                });

                if (!itemOwner.isSilent()) {
                    itemOwner.playSound(ModSounds.MOLOTOV_THROW.get(), 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
                }
            }
            if (proItem == ModItems.BLADE_HAT.get()) {
                BladeHatEntity hat = new BladeHatEntity(world, itemOwner, item);
                hat.setPos(hat.getX(), itemOwner.getY(0.5D) + 0.5D, hat.getZ() + userView.z * 0.75D);
                hat.shootFromRotation(itemOwner, 1.5F, 0.5F);
                hat.setBaseDamage(hat.getBaseDamage() / 6);
                hat.setOwner(user);
                TagServerSide(hat, lock_id, true);
                world.addFreshEntity(hat);
                setCanUpdateServerSide(hat, false);
                setPositionLockingServerSide(hat, true);
                if (itemOwner instanceof PlayerEntity) {
                    ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, 3);
                }
            }
            if (proItem == Items.TRIDENT) {
                TridentItem tridentItem = (TridentItem) proItem;
                int i = tridentItem.getUseDuration(item) - itemOwner.getUseItemRemainingTicks();
                if (i >= 10) {
                    int j = EnchantmentHelper.getRiptide(item);
                    if (j <= 0 || itemOwner.isInWaterOrRain()) {
                        item.hurtAndBreak(1, itemOwner, (PUser) -> {
                            PUser.broadcastBreakEvent(itemOwner.getUsedItemHand());
                        });
                        TridentEntity trident = new TridentEntity(world, itemOwner, item);
                        trident.setPos(trident.getX(), itemOwner.getY(0.5D) + 0.5D, trident.getZ() + userView.z * 0.75D);
                        trident.shootFromRotation(itemOwner, itemOwner.xRot, itemOwner.yRot, 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
                        trident.setBaseDamage(trident.getBaseDamage() / 6);
                        trident.setOwner(user);
                        TagServerSide(trident, lock_id, true);
                        if (itemOwner instanceof PlayerEntity) {
                            PlayerEntity player = (PlayerEntity) itemOwner;
                            if (player.abilities.instabuild) {
                                trident.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                            }
                            player.getCooldowns().addCooldown(proItem, 5);
                            player.awardStat(Stats.ITEM_USED.get(proItem));
                        }
                        world.addFreshEntity(trident);
                        setCanUpdateServerSide(trident, false);
                        setPositionLockingServerSide(trident, true);
                    }
                }
            }
            if (proItem == Items.IRON_NUGGET) {
                TommyGunBulletEntity bullet = new TommyGunBulletEntity(itemOwner, world);
                bullet.setPos(bullet.getX() + userView.x * 0.5D, itemOwner.getY(0.5D) + 0.5D, bullet.getZ() + userView.z * 0.5D);
                bullet.shootFromRotation(itemOwner, 1.5F, 0.0F);
                bullet.setOwner(user);
                TagServerSide(bullet, lock_id, true);
                world.addFreshEntity(bullet);
                setCanUpdateServerSide(bullet, false);
                setPositionLockingServerSide(bullet, true);
                if (itemOwner instanceof PlayerEntity) {
                    ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, 3);
                }
            }
            if (proItem == Items.FIRE_CHARGE) {
                double d0 = itemOwner.getX() + (userView.x * 0.3F);
                double d1 = itemOwner.getY() + (userView.y * 0.3F);
                double d2 = itemOwner.getZ() + (userView.z * 0.3F);
                double d3 = userView.x;
                double d4 = userView.y;
                double d5 = userView.z;
                SmallFireballEntity fireBall = new SmallFireballEntity(world, d0, d1, d2, d3, d4, d5);
                fireBall.setPos(fireBall.getX() + userView.x * 0.5D, itemOwner.getY(0.5D) + 0.5D, fireBall.getZ() + userView.z * 0.5D);
                fireBall.shootFromRotation(itemOwner, itemOwner.xRot, itemOwner.yRot, 0.0F, 1.5F, 1.0F);
                fireBall.setOwner(user);
                TagServerSide(fireBall, lock_id, true);
                world.addFreshEntity(fireBall);
                setCanUpdateServerSide(fireBall, false);
                setPositionLockingServerSide(fireBall, true);
                if (itemOwner instanceof PlayerEntity) {
                    ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, 10);
                }
            }
            if (proItem == Items.FIREWORK_ROCKET) {
                FireworkRocketEntity firework = new FireworkRocketEntity(world, item, itemOwner, itemOwner.getX(), itemOwner.getEyeY() - (double)0.15F, itemOwner.getZ(), true);
                firework.setPos(firework.getX() + userView.x * 0.5D, itemOwner.getY(0.5D) + 0.5D, firework.getZ() + userView.z * 0.5D);
                firework.shootFromRotation(itemOwner, itemOwner.xRot, itemOwner.yRot, 0.0F, 1.5F, 1.0F);
                firework.setOwner(user);
                TagServerSide(firework, lock_id, true);
                world.addFreshEntity(firework);
                setCanUpdateServerSide(firework, false);
                setPositionLockingServerSide(firework, true);
                if (itemOwner instanceof PlayerEntity) {
                    ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, 10);
                }
            }
            if (proItem == Items.ENDER_PEARL) {
                EnderPearlEntity enderPearl = new EnderPearlEntity(world, itemOwner);
                enderPearl.setItem(item);
                enderPearl.setPos(enderPearl.getX() + userView.x * 0.5D, itemOwner.getY(0.5D) + 0.5D, enderPearl.getZ() + userView.z * 0.5D);
                enderPearl.shootFromRotation(itemOwner, itemOwner.xRot, itemOwner.yRot, 0.0F, 1.5F, 1.0F);
                if (itemOwner instanceof StandEntity) {
                    enderPearl.setOwner(((StandEntity)itemOwner).getUser());
                }
                TagServerSide(enderPearl, lock_id, true);
                world.addFreshEntity(enderPearl);
                setCanUpdateServerSide(enderPearl, false);
                setPositionLockingServerSide(enderPearl, true);
                if (itemOwner instanceof PlayerEntity) {
                    ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, 20);
                    ((PlayerEntity)itemOwner).awardStat(Stats.ITEM_USED.get(proItem));
                }
            }
            if (proItem == Items.SNOWBALL) {
                SnowballEntity snowBall = new SnowballEntity(world, itemOwner);
                snowBall.setItem(item);
                snowBall.setPos(snowBall.getX() + userView.x * 0.5D, itemOwner.getY(0.5D) + 0.5D, snowBall.getZ() + userView.z * 0.5D);
                snowBall.shootFromRotation(itemOwner, itemOwner.xRot, itemOwner.yRot, 0.0F, 1.5F, 1.0F);
                snowBall.setOwner(user);
                TagServerSide(snowBall, lock_id, true);
                world.addFreshEntity(snowBall);
                setCanUpdateServerSide(snowBall, false);
                setPositionLockingServerSide(snowBall, true);
                if (itemOwner instanceof PlayerEntity) {
                    ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, 3);
                    ((PlayerEntity)itemOwner).awardStat(Stats.ITEM_USED.get(proItem));
                }
            }
            if (proItem == Items.EGG) {
                EggEntity egg = new EggEntity(world, itemOwner);
                egg.setItem(item);
                egg.setPos(egg.getX() + userView.x * 0.5D, itemOwner.getY(0.5D) + 0.5D, egg.getZ() + userView.z * 0.5D);
                egg.shootFromRotation(itemOwner, itemOwner.xRot, itemOwner.yRot, 0.0F, 0.75F, 1.0F);
                egg.setOwner(user);
                TagServerSide(egg, lock_id, true);
                world.addFreshEntity(egg);
                setCanUpdateServerSide(egg, false);
                setPositionLockingServerSide(egg, true);
                if (itemOwner instanceof PlayerEntity) {
                    ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, 3);
                    ((PlayerEntity)itemOwner).awardStat(Stats.ITEM_USED.get(proItem));
                }
            }
            if (proItem == Items.EXPERIENCE_BOTTLE) {
                ExperienceBottleEntity exp = new ExperienceBottleEntity(world, itemOwner);
                exp.setItem(item);
                exp.setPos(exp.getX() + userView.x * 0.5D, itemOwner.getY(0.5D) + 0.5D, exp.getZ() + userView.z * 0.5D);
                exp.shootFromRotation(itemOwner, itemOwner.xRot, itemOwner.yRot, 0.0F, 0.75F, 1.0F);
                exp.setOwner(user);
                TagServerSide(exp, lock_id, true);
                world.addFreshEntity(exp);
                setCanUpdateServerSide(exp, false);
                setPositionLockingServerSide(exp, true);
                if (itemOwner instanceof PlayerEntity) {
                    ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, 3);
                }
            }
        }
        else if (item.getItem() != Items.ENDER_EYE) {
            Item proItem = item.getItem();
            KWItemEntity projectileItem = new KWItemEntity(itemOwner, world);
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
            if (doNotRemove(proItem, item)) {
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
                else if (proItem instanceof ArmorItem && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.THORNS, item) > 0) {
                    projectileItem.setBaseDamage(projectileItem.getBaseDamage() + 1);
                }
            }
            if (removeOnImpact(proItem)) {
                projectileItem.removeAfterHitting(true);
            }
            if (proItem == Items.TOTEM_OF_UNDYING) {
                projectileItem.setBeneficial(true);
            }
            projectileItem.setPos(projectileItem.getX() + userView.x * 0.5D, itemOwner.getY(0.5D) + 0.5D, projectileItem.getZ() + userView.z * 0.5D);
            projectileItem.shootFromRotation(itemOwner, itemOwner.xRot, itemOwner.yRot, 0.0F, 1.5F, 1.0F);
            projectileItem.setBaseDamage(projectileItem.getBaseDamage() / 6);
            projectileItem.setOwner(user);
            TagServerSide(projectileItem, lock_id, true);
            world.addFreshEntity(projectileItem);
            setCanUpdateServerSide(projectileItem, false);
            setPositionLockingServerSide(projectileItem, true);
            if (itemOwner instanceof PlayerEntity) {
                ((PlayerEntity)itemOwner).getCooldowns().addCooldown(proItem, 3);
            }
        }
        else {
            if (world instanceof ServerWorld) {
                BlockPos blockpos = ((ServerWorld)world).getChunkSource().getGenerator().findNearestMapFeature((ServerWorld)world, Structure.STRONGHOLD, itemOwner.blockPosition(), 100, false);
                if (blockpos != null) {
                    EyeOfEnderEntity eyeOfEnder = new EyeOfEnderEntity(world, itemOwner.getX(), itemOwner.getY(0.5D), itemOwner.getZ());
                    eyeOfEnder.setItem(item);
                    eyeOfEnder.signalTo(blockpos);
                    TagServerSide(eyeOfEnder, lock_id, true);
                    world.addFreshEntity(eyeOfEnder);
                    setCanUpdateServerSide(eyeOfEnder, false);
                    setPositionLockingServerSide(eyeOfEnder, true);
                    if (itemOwner instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity) itemOwner;
                        if (player instanceof ServerPlayerEntity) {
                            CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayerEntity)player, blockpos);
                        }
                        player.awardStat(Stats.ITEM_USED.get(item.getItem()));
                        player.getCooldowns().addCooldown(item.getItem(), 3);
                    }
                    world.levelEvent((PlayerEntity)null, 1003, itemOwner.blockPosition(), 0);
                }
            }
        }
        if (!(itemOwner instanceof PlayerEntity && ((PlayerEntity)itemOwner).abilities.instabuild)) {
            item.shrink(itemsToThrow);
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
        return power.getUser() != null && !projectileList(item, power.getUser());
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

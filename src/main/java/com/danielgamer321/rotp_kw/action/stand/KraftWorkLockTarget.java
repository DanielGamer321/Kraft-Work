package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.capability.entity.EntityUtilCapProvider;
import com.danielgamer321.rotp_kw.entity.KWBlockEntity;
import com.danielgamer321.rotp_kw.init.InitEffects;
import com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.client.InputHandler;
import com.github.standobyte.jojo.entity.RoadRollerEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.item.GlovesItem;
import com.github.standobyte.jojo.item.SatiporojaScarfItem;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.LazySupplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.horse.TraderLlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class KraftWorkLockTarget extends StandAction {
    public KraftWorkLockTarget(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        switch (target.getType()) {
        case ENTITY:
            Entity entity = target.getEntity();
            boolean PositionLocking = entity.getCapability(EntityUtilCapProvider.CAPABILITY).map(cap -> cap.getPositionLocking()).orElse(false);
            if (!PositionLocking) {
                if (entity instanceof LivingEntity) {
                    LivingEntity targetEntity = (LivingEntity) entity;
                    if (targetEntity instanceof StandEntity || (targetEntity instanceof PlayerEntity &&
                            ((PlayerEntity)targetEntity).abilities.instabuild)) {
                        return ActionConditionResult.NEGATIVE;
                    }
                    else if (!EntitiesWithItems(targetEntity)) {
                        return conditionMessage("target_without_equipment");
                    }
                }
                else if (!(entity instanceof BoatEntity || entity instanceof AbstractMinecartEntity ||
                        entity instanceof TNTEntity || entity instanceof FallingBlockEntity ||
                        entity instanceof RoadRollerEntity)) {
                    return ActionConditionResult.NEGATIVE;
                }
            }
            else {
                return ActionConditionResult.NEGATIVE;
            }
            break;
        case BLOCK:
            BlockPos blockPos = target.getBlockPos();
            if (blockPos == null || user.level.isEmptyBlock(blockPos)) {
                return ActionConditionResult.NEGATIVE;
            }
            break;
        case EMPTY:
            return ActionConditionResult.NEGATIVE;
        default:
            break;
        }
        return super.checkSpecificConditions(user, power, target);
    }

    private boolean EntitiesWithItems(LivingEntity target) {
        ItemStack main = target.getItemBySlot(EquipmentSlotType.MAINHAND);
        ItemStack off = target.getItemBySlot(EquipmentSlotType.OFFHAND);
        ItemStack helmet = target.getItemBySlot(EquipmentSlotType.HEAD);
        ItemStack chestplace = target.getItemBySlot(EquipmentSlotType.CHEST);
        ItemStack leggings = target.getItemBySlot(EquipmentSlotType.LEGS);
        ItemStack boots = target.getItemBySlot(EquipmentSlotType.FEET);
        return !main.isEmpty() || !off.isEmpty() || !helmet.isEmpty() || !chestplace.isEmpty() ||
                !leggings.isEmpty() || !boots.isEmpty() ||
                target instanceof ArmorStandEntity ||
                target instanceof RavagerEntity ||
                (target instanceof LlamaEntity && ((LlamaEntity)target).getSwag() != null) ||
                target instanceof TraderLlamaEntity ||
                (target instanceof PigEntity && ((PigEntity)target).isSaddled()) ||
                (target instanceof StriderEntity && ((StriderEntity)target).isSaddled()) ||
                (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isSaddled()) ||
                (target instanceof AbstractChestedHorseEntity && ((AbstractChestedHorseEntity)target).hasChest());
    }

    private boolean TwohandItems(Item item){
        return item instanceof GlovesItem ||
                item instanceof BowItem ||
                item instanceof CrossbowItem ||
                item instanceof SatiporojaScarfItem;
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide()) {
            String lock_id = String.valueOf(user.getUUID());
            switch(target.getType()) {
            case BLOCK:
                BlockPos blockPos = target.getBlockPos();
                if (blockPos != null) {
                    world.getEntitiesOfClass(KWBlockEntity.class,
                            new AxisAlignedBB(Vector3d.atCenterOf(blockPos), Vector3d.atCenterOf(blockPos))).forEach(Entity::remove);
                    KWBlockEntity lockedPosition = new KWBlockEntity(world, target.getBlockPos());
                    lockedPosition.setLockedPosition(user);
                    KraftWorkStandType.TagServerSide(lockedPosition, lock_id, true);
                    world.addFreshEntity(lockedPosition);
                }
                break;
            case ENTITY:
                Entity entity = target.getEntity();
                if (entity instanceof LivingEntity && !(entity instanceof ArmorStandEntity)) {
                    LivingEntity living = (LivingEntity) target.getEntity();
                    if (EntitiesWithItems(living)) {
                        ItemStack main = living.getItemBySlot(EquipmentSlotType.MAINHAND);
                        ItemStack off = living.getItemBySlot(EquipmentSlotType.OFFHAND);
                        ItemStack helmet = living.getItemBySlot(EquipmentSlotType.HEAD);
                        ItemStack chestplace = living.getItemBySlot(EquipmentSlotType.CHEST);
                        ItemStack leggings = living.getItemBySlot(EquipmentSlotType.LEGS);
                        ItemStack boots = living.getItemBySlot(EquipmentSlotType.FEET);
                        IStandPower.getStandPowerOptional(user).ifPresent(stand -> {
                            int cost = 0;
                            if (TwohandItems(main.getItem()) || TwohandItems(off.getItem())) {
                                living.addEffect(new EffectInstance(InitEffects.LOCKED_MAIN_HAND.get(), 19999980, 0, false, false, true));
                                living.addEffect(new EffectInstance(InitEffects.LOCKED_OFF_HAND.get(), 19999980, 0, false, false, true));
                                cost = cost + 1;
                            }
                            else {
                                if (!main.isEmpty()) {
                                    living.addEffect(new EffectInstance(InitEffects.LOCKED_MAIN_HAND.get(), 19999980, 0, false, false, true));
                                    cost = cost + 1;
                                }
                                if (!off.isEmpty()) {
                                    living.addEffect(new EffectInstance(InitEffects.LOCKED_OFF_HAND.get(), 19999980, 0, false, false, true));
                                    cost = cost + 1;
                                }
                            }
                            if (!helmet.isEmpty()) {
                                binding(user, false, helmet);
                                living.addEffect(new EffectInstance(InitEffects.LOCKED_HELMET.get(), 19999980, 0, false, false, true));
                                cost = cost + 1;
                            }
                            if (!chestplace.isEmpty()) {
                                if (living instanceof AbstractHorseEntity) {
                                    living.addEffect(new EffectInstance(InitEffects.FULL_TRANSPORT_LOCKED.get(), 19999980, 0, false, false, true));
                                    cost = cost + 1;
                                }
                                else {
                                    binding(user, false, chestplace);
                                    living.addEffect(new EffectInstance(InitEffects.LOCKED_CHESTPLATE.get(), 19999980, 0, false, false, true));
                                    cost = cost + 1;
                                }
                            }
                            if (!leggings.isEmpty()) {
                                binding(user, false, leggings);
                                living.addEffect(new EffectInstance(InitEffects.LOCKED_LEGGINGS.get(), 19999980, 0, false, false, true));
                                cost = cost + 1;
                            }
                            if (!boots.isEmpty()) {
                                binding(user, false, boots);
                                living.addEffect(new EffectInstance(InitEffects.LOCKED_POSITION.get(), 19999980, 0, false, false, true));
                                cost = cost + 1;
                            }
                            if (living instanceof RavagerEntity ||
                                    living instanceof PigEntity && ((PigEntity)living).isSaddled() ||
                                    living instanceof AbstractChestedHorseEntity && ((AbstractChestedHorseEntity)living).hasChest()) {
                                living.addEffect(new EffectInstance(InitEffects.TRANSPORT_LOCKED.get(), 19999980, 0, false, false, true));
                                cost = cost + 1;
                            }
                            if (living instanceof StriderEntity && ((StriderEntity)living).isSaddled() ||
                                    living instanceof AbstractHorseEntity && ((AbstractHorseEntity)living).isSaddled() ||
                                    living instanceof LlamaEntity && ((LlamaEntity)living).getSwag() != null ||
                                    living instanceof TraderLlamaEntity) {
                                living.addEffect(new EffectInstance(InitEffects.FULL_TRANSPORT_LOCKED.get(), 19999980, 0, false, false, true));
                                cost = cost + 1;
                            }
                            cost = 10 * (cost - 1);
                            stand.consumeStamina(cost);
                        });
                    }
                }
                else {
                    KraftWorkStandType.setCanUpdateServerSide(entity, false);
                }
                KraftWorkStandType.setPositionLockingServerSide(entity, true);
                KraftWorkStandType.TagServerSide(entity, lock_id, true);
                break;
            }
        }
    }

    public void binding(LivingEntity user, Boolean status, ItemStack armor) {
        KraftWorkLockYourself.binding(user, status, armor);
    }

    @Override
    public boolean cancelHeldOnGettingAttacked(IStandPower power, DamageSource dmgSource, float dmgAmount) {
        return true;
    }

    @Override
    public String getTranslationKey(IStandPower power, ActionTarget target) {
        String key = super.getTranslationKey(power, target);
        if (getTarget()) {
            key += ".alt";
        }
        return key;
    }

    private final LazySupplier<ResourceLocation> altTex =
            new LazySupplier<>(() -> makeIconVariant(this, "_alt"));
    @Override
    public ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        if (power != null && getTarget()) {
            return altTex.get();
        }
        else {
            return super.getIconTexturePath(power);
        }
    }

    private boolean getTarget() {
        RayTraceResult target = InputHandler.getInstance().mouseTarget;
        ActionTarget actionTarget = ActionTarget.fromRayTraceResult(target);
        BlockPos block = actionTarget.getBlockPos();
        return block != null;
    }



    @Deprecated
    private ResourceLocation altTexPath;
    @Deprecated
    @Override
    public ResourceLocation getTexture(IStandPower power) {
        ResourceLocation resLoc = getRegistryName();
        if (getTarget()) {
            if (altTexPath == null) {
                altTexPath = new ResourceLocation(resLoc.getNamespace(), resLoc.getPath() + "_alt");
            }
            resLoc = altTexPath;
        }
        return resLoc;
    }

    @Deprecated
    @Override
    public Stream<ResourceLocation> getTexLocationstoLoad() {
        ResourceLocation resLoc = getRegistryName();
        return Stream.of(resLoc, new ResourceLocation(resLoc.getNamespace(), resLoc.getPath() + "_alt"));
    }
}

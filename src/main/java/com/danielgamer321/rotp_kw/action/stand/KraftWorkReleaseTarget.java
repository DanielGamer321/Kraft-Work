package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.capability.entity.EntityUtilCapProvider;
import com.danielgamer321.rotp_kw.entity.KWBlockEntity;
import com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.LazySupplier;
import com.github.standobyte.jojo.util.mod.JojoModUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class KraftWorkReleaseTarget extends StandAction {
    private static final double DETECTION_RANGE = 13;
    public KraftWorkReleaseTarget(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (!(user instanceof PlayerEntity)) {
            return ActionConditionResult.NEGATIVE;
        }
        String lock_id = String.valueOf(user.getUUID());
        RayTraceResult rayTrace = JojoModUtil.rayTrace(user.getEyePosition(1.0F), user.getLookAngle(), DETECTION_RANGE,
                user.level, user, e -> !(e.is(user)), 0, 0);
        if (rayTrace.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult) rayTrace).getEntity();
            if (!entity.getTags().contains(lock_id)) {
                return ActionConditionResult.NEGATIVE;
            }
        }
        else if (rayTrace.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockResult = (BlockRayTraceResult) rayTrace;
            BlockPos blockPos = blockResult.getBlockPos();
            if (blockPos != null) {
                BlockState blockState = user.level.getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (block != Blocks.AIR) {
                    List<KWBlockEntity> lockedBlock = user.level.getEntitiesOfClass(KWBlockEntity.class,
                            new AxisAlignedBB(Vector3d.atCenterOf(blockPos), Vector3d.atCenterOf(blockPos)));
                    if (lockedBlock.isEmpty()) {
                        return ActionConditionResult.NEGATIVE;
                    }
                    else {
                        return ActionConditionResult.POSITIVE;
                    }
                }
            }
        }
        else {
            return ActionConditionResult.NEGATIVE;
        }
        return super.checkSpecificConditions(user, power, target);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        RayTraceResult rayTrace = JojoModUtil.rayTrace(user.getEyePosition(1.0F), user.getLookAngle(), DETECTION_RANGE,
                user.level, user, e -> !(e.is(user)), 0, 0);
        if (rayTrace.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockResult = (BlockRayTraceResult) rayTrace;
            BlockPos blockPos = blockResult.getBlockPos();
            if (blockPos != null) {
                world.getEntitiesOfClass(KWBlockEntity.class,
                        new AxisAlignedBB(Vector3d.atCenterOf(blockPos), Vector3d.atCenterOf(blockPos))).forEach(Entity::remove);
            }
        }
        else if (rayTrace.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult) rayTrace).getEntity();
            KraftWorkStandType.setPositionLockingServerSide(entity, false);
            entity.getCapability(EntityUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.setPositionLocking(false));
        }
    }

    @Override
    public String getTranslationKey(IStandPower power, ActionTarget target) {
        String key = super.getTranslationKey(power, target);
        LivingEntity user = power.getUser();
        if (getTarget(user)) {
            key += ".alt";
        }
        return key;
    }

    private final LazySupplier<ResourceLocation> altTex =
            new LazySupplier<>(() -> makeIconVariant(this, "_alt"));
    @Override
    public ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        if (power != null) {
            LivingEntity user = power.getUser();
            if (getTarget(user)) {
                return altTex.get();
            }
            else {
                return super.getIconTexturePath(power);
            }
        }
        else {
            return super.getIconTexturePath(power);
        }
    }

    private boolean getTarget(LivingEntity user) {
        RayTraceResult target = JojoModUtil.rayTrace(user.getEyePosition(1.0F), user.getLookAngle(), DETECTION_RANGE,
                user.level, user, e -> !(e.is(user)), 0, 0);
        if (target.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockResult = (BlockRayTraceResult) target;
            BlockPos block = blockResult.getBlockPos();
            return block != null;
        }
        return false;
    }



    @Deprecated
    private ResourceLocation altTexPath;
    @Deprecated
    @Override
    public ResourceLocation getTexture(IStandPower power) {
        LivingEntity user = power.getUser();
        ResourceLocation resLoc = getRegistryName();
        if (getTarget(user)) {
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

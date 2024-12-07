package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.capability.entity.LivingUtilCapProvider;
import com.danielgamer321.rotp_kw.network.PacketManager;
import com.danielgamer321.rotp_kw.network.packets.fromserver.TrSetLockStatusPacket;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.LazySupplier;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class KraftWorkBlockingItemsStatus extends StandAction {
    public KraftWorkBlockingItemsStatus(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (getStatus(power) && !(user instanceof PlayerEntity)) {
            return ActionConditionResult.NEGATIVE;
        }
        return super.checkSpecificConditions(user, power, target);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide) {
            if (!getStatus(power)) {
                setStatusServerSide(user, true);
            }
            else {
                setStatusServerSide(user, false);
            }
        }
    }

    public static void setStatusServerSide(LivingEntity user, boolean status) {
        user.getCapability(LivingUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.setBlockingItemsStatus(status));
        if (!user.level.isClientSide()) {
            PacketManager.sendToClientsTrackingAndSelf(new TrSetLockStatusPacket(user.getId(), status, false), user);
        }
    }

    private final LazySupplier<ResourceLocation> onTex =
            new LazySupplier<>(() -> makeIconVariant(this, "_on"));
    @Override
    public ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        if (power != null && getStatus(power)) {
            return onTex.get();
        }
        else {
            return super.getIconTexturePath(power);
        }
    }

    private boolean getStatus(IStandPower power) {
        LivingEntity user = power.getUser();
        return user.getCapability(LivingUtilCapProvider.CAPABILITY).map(cap -> cap.getBlockingItemsStatus()).orElse(false);
    }



    @Deprecated
    private ResourceLocation onTexPath;
    @Deprecated
    @Override
    public ResourceLocation getTexture(IStandPower power) {
        ResourceLocation resLoc = getRegistryName();
        if (getStatus(power)) {
            if (onTexPath == null) {
                onTexPath = new ResourceLocation(resLoc.getNamespace(), resLoc.getPath() + "_on");
            }
            resLoc = onTexPath;
        }
        return resLoc;
    }

    @Deprecated
    @Override
    public Stream<ResourceLocation> getTexLocationstoLoad() {
        ResourceLocation resLoc = getRegistryName();
        return Stream.of(resLoc, new ResourceLocation(resLoc.getNamespace(), resLoc.getPath() + "_on"));
    }
}

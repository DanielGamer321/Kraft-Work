package com.danielgamer321.rotp_kw.power.impl.stand.type;

import com.danielgamer321.rotp_kw.init.InitEffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class LockedPosition {
    private UUID standUserId;
    private Entity standUser;
    private boolean shouldBeRemoved = false;

    public LockedPosition(@Nullable LivingEntity standUser) {
        if (standUser != null) {
            this.standUserId = standUser.getUUID();
        }
    }
    
    public void tick(World world, AxisAlignedBB aabb) {
        if (!world.isClientSide()) {
            LivingEntity user = (LivingEntity) this.getUser((ServerWorld) world);
            if (user != null && user.isAlive()) {
                List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, aabb, EntityPredicates.NO_CREATIVE_OR_SPECTATOR);
                for (LivingEntity target : entities) {
                    if (target.isAlive() && !target.getUUID().equals(standUserId) && !(target instanceof CreeperEntity && ((CreeperEntity) target).getSwellDir() >= 1)) {
                        target.addEffect(new EffectInstance(InitEffects.LOCKED_POSITION.get(), 3, 0, false, false, true));
                    }
                }
            }
            else {
                shouldBeRemoved = true;
            }
        }
    }

    public Entity getUser(ServerWorld world) {
        if (standUser == null && world instanceof ServerWorld) {
            standUser = ((ServerWorld) world).getEntity(standUserId);
        }
        return standUser;
    }

    public boolean shouldBeRemoved() {
        return shouldBeRemoved;
    }
    
    
    
    public static LockedPosition fromNBT(CompoundNBT nbt) {
        LockedPosition lock = new LockedPosition(null);
        lock.shouldBeRemoved = nbt.getBoolean("ShouldBeRemoved");
        if (nbt.hasUUID("StandUser")) {
            lock.standUserId = nbt.getUUID("StandUser");
        }
        return lock;
    }
    
    public CompoundNBT toNBT() {
        CompoundNBT lockNbt = new CompoundNBT();
        lockNbt.putBoolean("ShouldBeRemoved", shouldBeRemoved);
        if (standUserId != null) {
            lockNbt.putUUID("StandUser", standUserId);
        }
        return lockNbt;
    }

}

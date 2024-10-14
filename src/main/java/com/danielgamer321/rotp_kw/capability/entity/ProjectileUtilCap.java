package com.danielgamer321.rotp_kw.capability.entity;

import com.danielgamer321.rotp_kw.network.PacketManager;
import com.danielgamer321.rotp_kw.network.packets.fromserver.TrFlightTicksPacket;

import com.danielgamer321.rotp_kw.network.packets.fromserver.TrKineticEnergyPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class ProjectileUtilCap {
    private final Entity projectile;
    private int accumulation;
    private int plFlightTicks;
    private boolean readyToRelease;
    
    public ProjectileUtilCap(Entity entity) {
        this.projectile = entity;
    }

    public void setKineticEnergy(int accumulation) {
        accumulation = Math.max(accumulation, 0);
        if (this.accumulation != accumulation) {
            this.accumulation = accumulation;
            if (!projectile.level.isClientSide()) {
                PacketManager.sendToClientsTrackingAndSelf(new TrKineticEnergyPacket(projectile.getId(), accumulation), projectile);
            }
        }
    }

    public void addkineticEnergy() {
        setKineticEnergy(accumulation + 10);
    }

    public int getKineticEnergy() {
        return accumulation;
    }

    public void setFlightTicks(int ticks) {
        ticks = Math.max(ticks, 0);
        if (this.plFlightTicks != ticks) {
            this.plFlightTicks = ticks;
            if (!projectile.level.isClientSide()) {
                PacketManager.sendToClientsTrackingAndSelf(new TrFlightTicksPacket(projectile.getId(), ticks), projectile);
            }
        }
    }

    public void removePLFlightTicks() {
        setFlightTicks(plFlightTicks - 1);
    }

    public int getFlightTicks() {
        return plFlightTicks;
    }

    public void setReadyToRelease(boolean ready) {
        this.readyToRelease = ready;
    }

    public boolean getReadyToRelease() {
        return readyToRelease;
    }


    public void onTracking(ServerPlayerEntity tracking) {
        PacketManager.sendToClient(new TrKineticEnergyPacket(projectile.getId(), accumulation), tracking);
        PacketManager.sendToClient(new TrFlightTicksPacket(projectile.getId(), plFlightTicks), tracking);
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("KineticEnergy", accumulation);
        nbt.putInt("PLFlightTicks", plFlightTicks);
        nbt.putBoolean("ReadyToRelease", readyToRelease);
        return nbt;
    }

    public void fromNBT(CompoundNBT nbt) {
        this.accumulation = nbt.getInt("KineticEnergy");
        this.plFlightTicks = nbt.getInt("PLFlightTicks");
        this.readyToRelease = nbt.getBoolean("ReadyToRelease");
    }
}

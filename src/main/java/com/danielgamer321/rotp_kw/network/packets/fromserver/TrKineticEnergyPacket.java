package com.danielgamer321.rotp_kw.network.packets.fromserver;

import com.danielgamer321.rotp_kw.capability.entity.ProjectileUtilCapProvider;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TrKineticEnergyPacket {
    private final int entityId;
    private final int kineticEnergy;

    public TrKineticEnergyPacket(int entityId, int accumulation) {
        this.entityId = entityId;
        this.kineticEnergy = accumulation;
    }
    
    
    
    public static class Handler implements IModPacketHandler<TrKineticEnergyPacket> {

        @Override
        public void encode(TrKineticEnergyPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeVarInt(msg.kineticEnergy);
        }

        @Override
        public TrKineticEnergyPacket decode(PacketBuffer buf) {
            return new TrKineticEnergyPacket(buf.readInt(), buf.readVarInt());
        }

        @Override
        public void handle(TrKineticEnergyPacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity != null) {
                entity.getCapability(ProjectileUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.setKineticEnergy(msg.kineticEnergy));
            }
        }

        @Override
        public Class<TrKineticEnergyPacket> getPacketClass() {
            return TrKineticEnergyPacket.class;
        }
    }

}

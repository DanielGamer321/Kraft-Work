package com.danielgamer321.rotp_kw.network.packets.fromserver;

import com.danielgamer321.rotp_kw.capability.entity.ProjectileUtilCapProvider;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TrFlightTicksPacket {
    private final int entityId;
    private final int FlightTicks;

    public TrFlightTicksPacket(int entityId, int ticks) {
        this.entityId = entityId;
        this.FlightTicks = ticks;
    }
    
    
    
    public static class Handler implements IModPacketHandler<TrFlightTicksPacket> {

        @Override
        public void encode(TrFlightTicksPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeVarInt(msg.FlightTicks);
        }

        @Override
        public TrFlightTicksPacket decode(PacketBuffer buf) {
            return new TrFlightTicksPacket(buf.readInt(), buf.readVarInt());
        }

        @Override
        public void handle(TrFlightTicksPacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity != null) {
                entity.getCapability(ProjectileUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.setFlightTicks(msg.FlightTicks));
            }
        }

        @Override
        public Class<TrFlightTicksPacket> getPacketClass() {
            return TrFlightTicksPacket.class;
        }
    }

}

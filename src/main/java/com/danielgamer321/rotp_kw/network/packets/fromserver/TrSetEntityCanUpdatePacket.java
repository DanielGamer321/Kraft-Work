package com.danielgamer321.rotp_kw.network.packets.fromserver;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TrSetEntityCanUpdatePacket {
    private final int entityId;
    private final boolean canUpdate;

    public TrSetEntityCanUpdatePacket(int entityId, boolean canUpdate) {
        this.entityId = entityId;
        this.canUpdate = canUpdate;
    }
    
    
    
    public static class Handler implements IModPacketHandler<TrSetEntityCanUpdatePacket> {

        @Override
        public void encode(TrSetEntityCanUpdatePacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeBoolean(msg.canUpdate);
        }

        @Override
        public TrSetEntityCanUpdatePacket decode(PacketBuffer buf) {
            return new TrSetEntityCanUpdatePacket(buf.readInt(), buf.readBoolean());
        }

        @Override
        public void handle(TrSetEntityCanUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity != null) {
                entity.canUpdate(msg.canUpdate);
            }
        }

        @Override
        public Class<TrSetEntityCanUpdatePacket> getPacketClass() {
            return TrSetEntityCanUpdatePacket.class;
        }
    }

}

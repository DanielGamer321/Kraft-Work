package com.danielgamer321.rotp_kw.network.packets.fromserver;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TrTagPacket {
    private final int entityId;
    private final String tag;
    private final Boolean addOrRemove;

    public TrTagPacket(int entityId, String tag, Boolean addOrRemove) {
        this.entityId = entityId;
        this.tag = tag;
        this.addOrRemove = addOrRemove;
    }



    public static class Handler implements IModPacketHandler<TrTagPacket> {

        @Override
        public void encode(TrTagPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeUtf(msg.tag);
            buf.writeBoolean(msg.addOrRemove);
        }

        @Override
        public TrTagPacket decode(PacketBuffer buf) {
            return new TrTagPacket(buf.readInt(), buf.readUtf(), buf.readBoolean());
        }

        @Override
        public void handle(TrTagPacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity != null) {
                if (msg.addOrRemove) {
                    entity.addTag(msg.tag);
                }
                else {
                    entity.removeTag(msg.tag);
                }
            }
        }

        @Override
        public Class<TrTagPacket> getPacketClass() {
            return TrTagPacket.class;
        }

    }
}

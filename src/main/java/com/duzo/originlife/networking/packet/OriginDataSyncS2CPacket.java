package com.duzo.originlife.networking.packet;

import com.duzo.originlife.client.ClientOriginData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OriginDataSyncS2CPacket {
    private final int origin;

    public OriginDataSyncS2CPacket(int origin) {
        this.origin = origin;
    }

    public OriginDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.origin = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(origin);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientOriginData.set(origin);
        });
        return true;
    }
}

package com.duzo.originlife.networking.packet;

import com.duzo.originlife.client.ClientOriginData;
import com.duzo.originlife.client.ClientTimeData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TimeDataSyncS2CPacket {
    private final int seconds;

    public TimeDataSyncS2CPacket(int seconds) {
        this.seconds = seconds;
    }

    public TimeDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.seconds = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(seconds);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientTimeData.set(seconds);
        });
        return true;
    }
}

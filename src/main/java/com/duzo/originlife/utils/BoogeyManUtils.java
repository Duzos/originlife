package com.duzo.originlife.utils;

import com.duzo.originlife.config.CommonConfigs;
import com.duzo.originlife.networking.Network;
import com.duzo.originlife.networking.packet.OriginDataSyncS2CPacket;
import com.duzo.originlife.networking.packet.TimeDataSyncS2CPacket;
import com.duzo.originlife.origins.PlayerOriginsProvider;
import com.duzo.originlife.times.PlayerTime;
import com.duzo.originlife.times.PlayerTimeProvider;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.apache.logging.log4j.core.jmx.Server;

import javax.print.attribute.standard.Severity;
import java.util.Random;

public class BoogeyManUtils {
    public static ServerPlayer boogeyMan = null;
    public static void tick(MinecraftServer server) {
        if (!boogeyManPicked() && server.getPlayerList().getPlayers().size() > 1) {
            if(new Random().nextFloat() <= CommonConfigs.BOOGEYMAN_CHANCE.get()) {
                // Pick new boogeyman
                pickBoogeyMan(server);
            }
        }
    }
    public static ServerPlayer pickBoogeyMan(MinecraftServer server) {
        Random rand = new Random();
        ServerPlayer player = server.getPlayerList().getPlayers().get(rand.nextInt(server.getPlayerList().getPlayers().size()));
        boogeyMan = player;
        sendChosenMessage();
        sendNotChosenMessages();
        return player;
    }
    public static void resetBoogeyMan() {
        boogeyMan = null;
    }
    public static boolean boogeyManPicked() {
        return boogeyMan != null;
    }
    public static boolean isBoogeyMan(ServerPlayer player) {
        return player == boogeyMan;
    }
    public static void sendChosenMessage() {
        if (!boogeyManPicked()) {
            LogUtils.getLogger().error("Tried to send boogeyman message without a boogeyman!");
            return;
        }

        MessagingUtils.sendChatMessage(boogeyMan,"You are the boogeyman!", ChatFormatting.RED);
    }
    public static void sendNotChosenMessages() {
        if (!boogeyManPicked()) {
            LogUtils.getLogger().error("Tried to send boogeyman message without a boogeyman!");
            return;
        }

        for (ServerPlayer player : boogeyMan.getServer().getPlayerList().getPlayers()) {
            if (isBoogeyMan(player)) continue;
            MessagingUtils.sendChatMessage(player,"You are NOT the boogeyman.",ChatFormatting.GREEN);
        }
    }
    public static void sendCureMessageAndClear() {
        if (!boogeyManPicked()) {
            LogUtils.getLogger().error("Tried to send boogeyman message without a boogeyman!");
            return;
        }

        MessagingUtils.sendChatMessage(boogeyMan,"You are cured!", ChatFormatting.GREEN);
        resetBoogeyMan();
    }
    public static void removeHours(ServerPlayer player) {
        player.getCapability(PlayerTimeProvider.PLAYER_TIME).ifPresent(time -> {
            time.subTimeHours(2);
            Network.sendToPlayer(new TimeDataSyncS2CPacket(time.getTime()),player);
        });
        MessagingUtils.sendChatMessage(player,"-2 hours",ChatFormatting.RED,ChatFormatting.BOLD);
    }
    public static void boogeyManLoses() {
        if (!boogeyManPicked()) {
            LogUtils.getLogger().error("Tried to send boogeyman things without a boogeyman!");
            return;
        }

        MessagingUtils.sendChatMessage(boogeyMan,"You have failed.",ChatFormatting.RED,ChatFormatting.BOLD);

        boogeyMan.getCapability(PlayerOriginsProvider.PLAYER_ORIGIN).ifPresent(origin -> {
            origin.setOrigin(1);
            Network.sendToPlayer(new OriginDataSyncS2CPacket(origin.getOrigin()),boogeyMan);
        });
        boogeyMan.getCapability(PlayerTimeProvider.PLAYER_TIME).ifPresent(time -> {
            time.setTimeHours(time.MAX_TIME);
            Network.sendToPlayer(new TimeDataSyncS2CPacket(time.getTime()),boogeyMan);
        });
        boogeyMan = null;
    }
}

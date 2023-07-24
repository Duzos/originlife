package com.duzo.originlife.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class MessagingUtils {
    public static void sendChatMessage(Player player, String message,ChatFormatting... formats) {
        player.sendSystemMessage(Component.literal(message).withStyle(formats));
    }
    public static void sendChatMessageToAll(MinecraftServer server, String message, ChatFormatting... formats) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            sendChatMessage(player,message,formats);
        }
    }
    public static void sendLoseHourMessage(Player player) {
        sendChatMessage(player,"-1 hour",ChatFormatting.RED,ChatFormatting.BOLD);
    }
    public static void sendPlayerLoseMessage(Player player) {
        if (player.getServer() == null) {
            LogUtils.getLogger().error(player.getName().getString() + "'s server was null! Cannot send message.");
            return;
        }

        sendChatMessage(player,player.getName().getString() + " has run out of lives!",ChatFormatting.GOLD,ChatFormatting.BOLD);
        sendChatMessageToAll(player.getServer(),player.getName().getString() + " has run out of lives!",ChatFormatting.GOLD,ChatFormatting.BOLD);
    }
}

package com.duzo.originlife.client.gui;

import com.duzo.originlife.client.ClientOriginData;
import com.duzo.originlife.client.ClientTimeData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import static com.duzo.originlife.utils.ColourUtils.getColourFromOrigin;
import static com.duzo.originlife.utils.TimeUtils.getDisplayText;

public class OriginLifeOverlay {
    public static final IGuiOverlay HUD_ORIGINLIFE = (((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        int x = screenWidth / 2;
        int y = screenHeight;

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;

        if (ClientOriginData.getPlayerOrigin() <= 0) return;

        String origin = "Origin: " + ClientOriginData.getPlayerOrigin();
        mc.font.drawShadow(poseStack, origin,(screenWidth / 2f) + 96,screenHeight - 22,getColourFromOrigin(ClientOriginData.getPlayerOrigin()));

        String time = "Time: " + getDisplayText(ClientTimeData.getPlayerTime());
        mc.font.drawShadow(poseStack,time,(screenWidth / 2f) + 96,screenHeight - 11,getColourFromOrigin(ClientOriginData.getPlayerOrigin()));
    }));
}

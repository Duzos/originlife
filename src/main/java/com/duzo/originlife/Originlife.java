package com.duzo.originlife;

import com.duzo.originlife.client.gui.OriginLifeOverlay;
import com.duzo.originlife.config.CommonConfigs;
import com.duzo.originlife.networking.Network;
import com.duzo.originlife.networking.packet.TimeDataSyncS2CPacket;
import com.duzo.originlife.origins.PlayerOriginsProvider;
import com.duzo.originlife.times.PlayerTimeProvider;
import com.duzo.originlife.utils.BoogeyManUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Originlife.MODID)
public class Originlife {

    public static final String MODID = "originlife";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Originlife() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigs.SPEC, "originlife-common.toml");

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        Network.register();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    int tick = 0;
    @SubscribeEvent
    public void scheduler(TickEvent.ServerTickEvent event) {
        if (tick != 40) {
            tick++;
            return;
        }

        tick = 0;

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            player.getCapability(PlayerTimeProvider.PLAYER_TIME).ifPresent(time -> {
                if (time.getTime() > 0) {
                    time.subTimeSeconds(1);
                    // Updating the client
                    Network.sendToPlayer(new TimeDataSyncS2CPacket(time.getTime()),player);
                }
                if (time.getTime() <= 0) {
                    player.getCapability(PlayerOriginsProvider.PLAYER_ORIGIN).ifPresent(origin -> {
                        if (origin.getOrigin() > 0) {
                            player.kill();
                        }
                    });
                }
            });
        }

        // Boogeyman ticking
        BoogeyManUtils.tick(event.getServer());
    }

    @SubscribeEvent
    public void death(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            // If the boogeyman kills a player he wins
            if (BoogeyManUtils.boogeyManPicked() && event.getSource().getEntity() instanceof Player && player instanceof ServerPlayer serverPlayer) {
                if (event.getSource().getDirectEntity().is(BoogeyManUtils.boogeyMan)) {
                    BoogeyManUtils.removeHours(serverPlayer);
                    BoogeyManUtils.sendCureMessageAndClear();
                }
            }
            // If the boogeyman dies any other way he loses
            if (BoogeyManUtils.boogeyManPicked() && player instanceof ServerPlayer serverPlayer) {
                if (BoogeyManUtils.isBoogeyMan(serverPlayer)) {
                    BoogeyManUtils.boogeyManLoses();
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
        @SubscribeEvent
        public static void renderOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("hud_originlife", OriginLifeOverlay.HUD_ORIGINLIFE);
        }
    }
}

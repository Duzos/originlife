package com.duzo.originlife.events;

import com.duzo.originlife.Originlife;
import com.duzo.originlife.networking.Network;
import com.duzo.originlife.networking.packet.OriginDataSyncS2CPacket;
import com.duzo.originlife.networking.packet.TimeDataSyncS2CPacket;
import com.duzo.originlife.origins.PlayerOrigins;
import com.duzo.originlife.origins.PlayerOriginsProvider;
import com.duzo.originlife.times.PlayerTime;
import com.duzo.originlife.times.PlayerTimeProvider;
import com.duzo.originlife.utils.MessagingUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.duzo.originlife.utils.TimeUtils.secondsToHours;

@Mod.EventBusSubscriber(modid = Originlife.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerOriginsProvider.PLAYER_ORIGIN).isPresent()) {
                event.addCapability(new ResourceLocation(Originlife.MODID,"origin"), new PlayerOriginsProvider());
            }
            if (!event.getObject().getCapability(PlayerTimeProvider.PLAYER_TIME).isPresent()) {
                event.addCapability(new ResourceLocation(Originlife.MODID,"time"), new PlayerTimeProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerOrigins.class);
        event.register(PlayerTime.class);
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerOriginsProvider.PLAYER_ORIGIN).ifPresent(origin -> {
                    Network.sendToPlayer(new OriginDataSyncS2CPacket(origin.getOrigin()),player);
                });
                player.getCapability(PlayerTimeProvider.PLAYER_TIME).ifPresent(time -> {
                    Network.sendToPlayer(new TimeDataSyncS2CPacket(time.getTime()),player);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // Preserving the old values
            event.getOriginal().reviveCaps();

            event.getOriginal().getCapability(PlayerOriginsProvider.PLAYER_ORIGIN).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerOriginsProvider.PLAYER_ORIGIN).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });

            event.getOriginal().getCapability(PlayerTimeProvider.PLAYER_TIME).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerTimeProvider.PLAYER_TIME).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });

            // Updating the new values correctly
            event.getEntity().getCapability(PlayerTimeProvider.PLAYER_TIME).ifPresent(time -> {
                // If dies with more than an hour, just remove one hour and dont change the origin and send a message
                if (secondsToHours(time.getTime()) > 1) {
                    time.subTimeHours(1);
                    // Updating the client

                    if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                        MessagingUtils.sendLoseHourMessage(serverPlayer);
                        Network.sendToPlayer(new TimeDataSyncS2CPacket(time.getTime()),serverPlayer);
                    }
                } else {
                    // Otherwise just remove an origin and reset the time
                    event.getEntity().getCapability(PlayerOriginsProvider.PLAYER_ORIGIN).ifPresent(origin -> {
                        origin.subOrigin(1);
                        // Updating the client

                        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                            Network.sendToPlayer(new OriginDataSyncS2CPacket(origin.getOrigin()),serverPlayer);
                        }

                        // If the origin is now 0, put them in spectator and send a message
                        if (origin.getOrigin() == 0) {
                            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                                MessagingUtils.sendPlayerLoseMessage(serverPlayer);
                                serverPlayer.setGameMode(GameType.SPECTATOR); // @TODO bug with collisions in spectator.
                            }
                        }
                    });
                    time.setTimeHours(3);
                    // Updating the client

                    if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                        Network.sendToPlayer(new TimeDataSyncS2CPacket(time.getTime()),serverPlayer);
                    }
                }
            });

        }
        event.getOriginal().invalidateCaps();
    }
}

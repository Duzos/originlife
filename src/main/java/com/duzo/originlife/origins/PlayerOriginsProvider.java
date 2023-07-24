package com.duzo.originlife.origins;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerOriginsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerOrigins> PLAYER_ORIGIN = CapabilityManager.get(new CapabilityToken<PlayerOrigins>() {});
    private PlayerOrigins origins = null;
    private final LazyOptional<PlayerOrigins> optional = LazyOptional.of(this::createPlayerOrigins);

    private PlayerOrigins createPlayerOrigins() {
        if (this.origins == null) {
            this.origins = new PlayerOrigins();
        }

        return this.origins;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_ORIGIN) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createPlayerOrigins().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerOrigins().loadNBTData(nbt);
    }
}

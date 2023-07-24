package com.duzo.originlife.times;


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

public class PlayerTimeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerTime> PLAYER_TIME = CapabilityManager.get(new CapabilityToken<PlayerTime>() {});
    private PlayerTime time = null;
    private final LazyOptional<PlayerTime> optional = LazyOptional.of(this::createPlayerTime);

    private PlayerTime createPlayerTime() {
        if (this.time == null) {
            this.time = new PlayerTime();
        }

        return this.time;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_TIME) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createPlayerTime().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerTime().loadNBTData(nbt);
    }
}

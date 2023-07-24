package com.duzo.originlife.origins;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

//@AutoRegisterCapability
public class PlayerOrigins {
    private final int MAX_ORIGIN = 8;
    private final int MIN_ORIGIN = 0;

    private int origin = MAX_ORIGIN;

    public int getOrigin() {
        return origin;
    }

    public void addOrigin(int add) {
        this.origin = Math.min(origin + add, MAX_ORIGIN);
    }
    public void subOrigin(int sub) {
        this.origin = Math.max(origin - sub, MIN_ORIGIN);
    }
    public void setOrigin(int val) {
        this.origin = Mth.clamp(val,MIN_ORIGIN,MAX_ORIGIN);
    }
    public void copyFrom(PlayerOrigins source) {
        this.origin = source.origin;
    }

    public void saveNBTData(CompoundTag tag) {
        tag.putInt("origin",origin);
    }
    public void loadNBTData(CompoundTag tag) {
        this.origin = tag.getInt("origin");
    }
}

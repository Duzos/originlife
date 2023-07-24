package com.duzo.originlife.times;

import com.duzo.originlife.config.CommonConfigs;
import com.duzo.originlife.origins.PlayerOrigins;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

public class PlayerTime {
    public final int MAX_TIME = CommonConfigs.LIFE_LENGTH_SECONDS.get(); // 3 hours in seconds is 10800
    private final int MIN_TIME = 0;
    private int time = MAX_TIME;

    public int getTime() {
        return time;
    }

    public void addTimeSeconds(int add) {
        this.time = Math.min(time + add, MAX_TIME);
    }
    public void subTimeSeconds(int sub) {
        this.time = Math.max(time - sub, MIN_TIME);
    }
    public void addTimeMinutes(int add) {
        this.addTimeSeconds(add * 60);
    }
    public void subTimeMinutes(int sub) {
        this.subTimeSeconds(sub * 60);
    }
    public void addTimeHours(int add) {
        this.addTimeMinutes(add * 60);
    }
    public void subTimeHours(int sub) {
        this.subTimeMinutes(sub * 60);
    }
    public void setTimeSeconds(int val) {
        this.time = Mth.clamp(val,MIN_TIME,MAX_TIME);
    }
    public void setTimeMinutes(int val) {
        setTimeSeconds(val * 60);
    }
    public void setTimeHours(int val) {
        setTimeMinutes(val * 60);
    }
    public void copyFrom(PlayerTime source) {
        this.time = source.time;
    }

    public void saveNBTData(CompoundTag tag) {
        tag.putInt("time",time);
    }
    public void loadNBTData(CompoundTag tag) {
        this.time = tag.getInt("time");
    }
}

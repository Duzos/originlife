package com.duzo.originlife.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> LIFE_LENGTH_SECONDS;
    public static final ForgeConfigSpec.ConfigValue<Float> BOOGEYMAN_CHANCE;

    static {
        BUILDER.push("Configs for OriginLife Mod");

        LIFE_LENGTH_SECONDS = BUILDER.comment("The amount of seconds per origin life")
                .define("LIFE LENGTH SECONDS",10800);
        BOOGEYMAN_CHANCE = BUILDER.comment("The chance for the boogeyman to be picked")
                .define("BOOGEYMAN_CHANCE",0.005f);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}

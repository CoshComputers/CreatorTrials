package com.dsd.ct.containers;


import com.dsd.ct.configs.MobSpawnConfig;

public class MobSpawnConfigContainer {
    private final MobSpawnConfig mobSpawnConfig;

    public MobSpawnConfigContainer(MobSpawnConfig mobSpawnConfig) {
        this.mobSpawnConfig = mobSpawnConfig;
    }

    public MobSpawnConfig getMobSpawnConfig() {
        return mobSpawnConfig;
    }



}
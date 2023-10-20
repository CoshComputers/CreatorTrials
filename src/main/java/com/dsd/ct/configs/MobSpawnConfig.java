package com.dsd.ct.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MobSpawnConfig {
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    @SerializedName("mobSpawnOverrides")
    private List<MobOverride> mobSpawnOverrides;

    public List<MobOverride> getMobSpawnOverrides() {
        return mobSpawnOverrides;
    }

    // Setter method for mobSpawnOverrides
    public void setMobSpawnOverrides(List<MobOverride> mobSpawnOverrides) {
        this.mobSpawnOverrides = mobSpawnOverrides;
    }

    // ... other methods if necessary


    public static class MobOverride {
        @SerializedName("blazeWeighting")
        private int blazeWeighting;
        @SerializedName("endermenWeighting")
        private int endermenWeighting;

        public MobOverride() {
            this.blazeWeighting = -1;
            this.endermenWeighting = -1;
        }

        public int getBlazeWeighting() {
            return blazeWeighting;
        }

        public int getEndermenWeighting() {
            return endermenWeighting;
        }

    }
}
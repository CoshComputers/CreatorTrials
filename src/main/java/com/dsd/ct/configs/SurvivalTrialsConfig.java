package com.dsd.ct.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SurvivalTrialsConfig {
    private static final List<String> commandList = new ArrayList<>();

    public SurvivalTrialsConfig(){
        commandList.add("overrideMobs");
        commandList.add("spawnGiants");
        commandList.add("giveInitialGear");
        commandList.add("giveSpecialLoot");
        commandList.add("usePlayerHeads");
        commandList.add("debugOn");
    }

    public static List<String> getCommandList() {
        return commandList;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    @SerializedName("survivalTrialsConfig")
    private MainConfig survivalTrialsConfig;

    public MainConfig getSurvivalTrialsMainConfig() {
        return survivalTrialsConfig;
    }

    public void setSurvivalTrialsMainConfig(MainConfig mainConfig) {
        this.survivalTrialsConfig = mainConfig;
    }

    public static class MainConfig {
        @SerializedName("overrideMobs")
        private boolean overrideMobs;
        @SerializedName("spawnGiants")
        private boolean spawnGiants;
        @SerializedName("giveInitialGear")
        private boolean giveInitialGear;
        @SerializedName("giveSpecialLoot")
        private boolean giveSpecialLoot;
        @SerializedName("usePlayerHeads")
        private boolean usePlayerHeads;

        @SerializedName("debugOn")
        private boolean debugOn;

        public boolean isOverrideMobs() {
            return overrideMobs;
        }

        public void setOverrideMobs(boolean overrideMobs) {
            this.overrideMobs = overrideMobs;
        }

        public boolean isSpawnGiants() {
            return spawnGiants;
        }

        public void setSpawnGiants(boolean spawnGiants) {
            this.spawnGiants = spawnGiants;
        }

        public boolean isGiveInitialGear() {
            return giveInitialGear;
        }

        public void setGiveInitialGear(boolean giveInitialGear) {
            this.giveInitialGear = giveInitialGear;
        }

        public boolean isGiveSpecialLoot() {
            return giveSpecialLoot;
        }

        public void setGiveSpecialLoot(boolean giveSpecialLoot) {
            this.giveSpecialLoot = giveSpecialLoot;
        }

        public boolean isUsePlayerHeads() {
            return usePlayerHeads;
        }

        public void setUsePlayerHeads(boolean usePlayerHeads) {
            this.usePlayerHeads = usePlayerHeads;
        }

        public boolean isDebugOn() {
            return debugOn;
        }

        public void setDebugOn(boolean debugOn) {
            this.debugOn = debugOn;
        }


    }
}

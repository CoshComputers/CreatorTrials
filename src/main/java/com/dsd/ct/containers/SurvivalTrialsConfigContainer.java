package com.dsd.ct.containers;


import com.dsd.ct.configs.SurvivalTrialsConfig;

public class SurvivalTrialsConfigContainer {
    private final SurvivalTrialsConfig survivalTrialsConfig;

    public SurvivalTrialsConfigContainer(SurvivalTrialsConfig survivalTrialsConfig) {
        this.survivalTrialsConfig = survivalTrialsConfig;
    }

    public SurvivalTrialsConfig getSurvivalTrialsConfig() {
        return survivalTrialsConfig;
    }
}
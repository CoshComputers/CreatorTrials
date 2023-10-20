package com.dsd.ct.containers;

import com.dsd.ct.configs.InitialGearConfig;
import com.dsd.ct.configs.InitialGearConfigWrapper;

public class InitialGearConfigContainer {
    private final InitialGearConfig initialGearConfig;

    public InitialGearConfigContainer(InitialGearConfigWrapper wrapper) {
        this.initialGearConfig = wrapper.getInitialGearConfig();
    }

    public InitialGearConfig getInitialGearConfig() {
        return initialGearConfig;
    }

}

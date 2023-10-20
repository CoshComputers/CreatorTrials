package com.dsd.ct.containers;

import com.dsd.ct.configs.GiantConfig;
import com.dsd.ct.configs.GiantConfigWrapper;

public class GiantConfigContainer {
    private final GiantConfig giantConfig;

    public GiantConfigContainer(GiantConfigWrapper wrapper) {
        this.giantConfig = wrapper.getGiantConfig();
    }

    public GiantConfig getGiantConfig() {
        return giantConfig;
    }
}

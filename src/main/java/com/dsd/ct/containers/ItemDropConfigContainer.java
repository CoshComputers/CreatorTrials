package com.dsd.ct.containers;

import com.dsd.ct.configs.ItemDropConfig;
import com.dsd.ct.configs.ItemDropConfigWrapper;

public class ItemDropConfigContainer {
    private final ItemDropConfig itemDropConfig;

    public ItemDropConfigContainer(ItemDropConfigWrapper wrapper) {
        this.itemDropConfig = wrapper.getItemDropConfig();
    }

    public ItemDropConfig getItemDropConfig() {
        return itemDropConfig;
    }

}

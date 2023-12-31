package com.dsd.ct.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InitialGearConfig {

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    private List<GearItem> initialGear;

    public InitialGearConfig() {
        initialGear = new ArrayList<>();
    }
    public List<GearItem> getInitialGear() {
        return initialGear;
    }

    public void setInitialGear(List<GearItem> initialGear) {
        this.initialGear = initialGear;
    }

    public static class GearItem {
        private String item;
        private Map<String, Integer> enchantments;


        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public Map<String, Integer> getEnchantments() {
            return enchantments;
        }

        public void setEnchantments(Map<String, Integer> enchantments) {
            this.enchantments = enchantments;
        }
    }
}

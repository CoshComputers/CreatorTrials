package com.dsd.ct.entities;

import com.dsd.ct.util.ModEnums;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class CreatorTrialsBabyZombie extends Zombie {
    private ModEnums.SkullDropMapping appearance;

    public CreatorTrialsBabyZombie(Level p_34274_) {
        super(p_34274_);
        this.setBaby(true);
        this.appearance = ModEnums.SkullDropMapping.UNKNOWN;
    }

    public CreatorTrialsBabyZombie(EntityType type, Level world) {

        super(type,world);
        this.setBaby(true);
        this.appearance = ModEnums.SkullDropMapping.UNKNOWN;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        // Add custom goals here
    }
    public ModEnums.SkullDropMapping getAppearance() {
        return appearance;
    }

    public void setAppearance(ModEnums.SkullDropMapping appearance) {
        this.appearance = appearance;
        ItemStack head = ItemStack.EMPTY;
        ItemStack handItem = ItemStack.EMPTY;
        switch(this.appearance) {
            case BLAZE:  // Blaze head and blaze rod
                head = getSkull("MHF_Blaze");
                handItem = new ItemStack(Items.BLAZE_ROD);
                break;
            case ENDERMAN:  // Enderman head and ender pearl
                head = getSkull("MHF_Enderman");
                handItem = new ItemStack(Items.ENDER_PEARL);
                break;
            default:
                break;
        }
        this.setItemSlot(EquipmentSlot.HEAD, head);
        this.setItemSlot(EquipmentSlot.MAINHAND, handItem);
    }


    private ItemStack getSkull(String skullOwner) {
        ItemStack skull = new ItemStack(Items.PLAYER_HEAD);
        CompoundTag tag = skull.getOrCreateTag();
        tag.putString("SkullOwner", skullOwner);
        return skull;
    }

    @Override
    public String toString() {
        return "CreatorTrialsBabyZombie{" +
                "appearance=" + appearance +
                '}';
    }
}

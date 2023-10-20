package com.dsd.ct.entities;

import com.dsd.ct.util.CustomLogger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class OverriddenMobType {
    public enum Appearance {
        NONE,
        BLAZE,
        ENDERMAN
    }
    private final EntityType<?> entityType;
    private final boolean isBaby;
    private final Appearance appearance;

    public OverriddenMobType(EntityType<?> entityType,
                             boolean isBaby, Appearance appearance) {
        this.entityType = entityType;
        this.isBaby = isBaby;
        this.appearance = appearance;
    }

    // ... other methods
    public Entity createEntity(ServerLevel level) {
        try {
            Entity entity = this.entityType.create(level);
            if (entity != null) {
                if (entity instanceof Zombie && this.isBaby) {
                    ((Zombie) entity).setBaby(true);
                }
                if (entity instanceof Monster) {
                    setAppearance((Monster) entity);
                }
                return entity;
            } else {
                // Log an error message if the entity could not be created
                CustomLogger.getInstance().error(String.format("Failed to create entity of type %s", this.entityType));
                return null;
            }
        } catch (Exception e) {
            // Log the exception
            CustomLogger.getInstance().error(String.format("Exception occurred while creating entity of type %s: %s", this.entityType, e.getMessage()));
            return null;
        }
    }

    private void setAppearance(Monster monsterEntity) {

        ItemStack head = ItemStack.EMPTY;
        ItemStack handItem = ItemStack.EMPTY;

        switch(this.appearance) {
            case BLAZE:  // Blaze head and blaze rod
                head = getSkull("MHF_Blaze");
                handItem = new ItemStack(Items.BLAZE_ROD);
                monsterEntity.setItemSlot(EquipmentSlot.HEAD, head);
                monsterEntity.setItemSlot(EquipmentSlot.MAINHAND, handItem);
                break;
            case ENDERMAN:  // Enderman head and ender pearl
                head = getSkull("MHF_Enderman");
                handItem = new ItemStack(Items.ENDER_PEARL);
                monsterEntity.setItemSlot(EquipmentSlot.HEAD, head);
                monsterEntity.setItemSlot(EquipmentSlot.MAINHAND, handItem);
                break;
            default:
                break;
        }

    }

    /*private void setRandomPlayerHead(MonsterEntity monsterEntity) {
        // Assume playerConfigs is a static field of type Map<UUID, PlayerConfig>
        if (!PlayerManager.getInstance().getAllPlayerConfigs().isEmpty()) {
            // Pick a random player UUID
            PlayerConfig randomPlayer = PlayerManager.getInstance().getRandomPlayer();
            ItemStack playerHead = randomPlayer.getPlayerHead();
            if(playerHead != null) {
                // Set the player's skin data on the head
                // Equip the monster entity with the player head
                monsterEntity.setItemSlot(EquipmentSlotType.HEAD, playerHead);
            }else{
                CustomLogger.getInstance().error(String.format("Failed to get Player head"));
            }
        }
    }*/

    private ItemStack getSkull(String skullOwner) {
        ItemStack skull = new ItemStack(Items.PLAYER_HEAD);
        CompoundTag tag = skull.getOrCreateTag();
        tag.putString("SkullOwner", skullOwner);
        return skull;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OverriddenMobType {");
        sb.append("entityType: ").append(entityType.toShortString());
        sb.append(", isBaby: ").append(isBaby);
        sb.append(", appearance: ").append(appearance.name());
        sb.append(" }");

        return sb.toString();
    }
}

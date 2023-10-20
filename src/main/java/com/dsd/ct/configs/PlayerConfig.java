package com.dsd.ct.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.UUID;

public class PlayerConfig {
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    @SerializedName("playerUuid")
    private final UUID playerUuid;

    @SerializedName("lastLogin")
    private Instant lastLogin;


    private String playerName;
    // No-arg constructor for Gson
    private PlayerConfig() {
        this.playerUuid = null;  // Gson will override this
    }

    // Constructor for new players
    public PlayerConfig(UUID playerUuid) {
        this.playerUuid = playerUuid;
        updateLastLogin();
    }

    // Method to update the lastLogin field
    public void updateLastLogin() {
        this.lastLogin = Instant.now();
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public void setPlayerName(String pName){
            this.playerName = pName;
    }
    public String getPlayerName(){
        return playerName;
    }

    /*public ItemStack getPlayerHead() {
        ItemStack playerHead = new ItemStack(Items.PLAYER_HEAD);  // Ensure PLAYER_HEAD is the correct item for your Minecraft version
        CompoundNBT nbt = new CompoundNBT();
        UUID testUUID = UUID.fromString("0db1ebd5-50e2-46e9-95fb-ffd49efcf79c");
        nbt.put("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), new GameProfile(testUUID, this.playerName)));
        playerHead.setTag(nbt);
        CustomLogger.getInstance().debug(String.format("player [%s] head NBT = %s",this.playerName,playerHead.getTag().toString()));
        return playerHead;
    }*/



}

package com.dsd.ct.managers;

import com.dsd.ct.configs.*;
import com.dsd.ct.containers.*;
import com.dsd.ct.util.CustomLogger;
import com.dsd.ct.util.EnumTypes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConfigManager {
    private static ConfigManager instance;
    private final Gson mainGson;
    private final Gson giantGson;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private InitialGearConfigContainer gearConfigContainer;
    private MobSpawnConfigContainer mobOverrideConfigContainer;
    private ItemDropConfigContainer itemDropConfigContainer;
    private GiantConfigContainer giantConfigContainer;
    private SurvivalTrialsConfigContainer survivalTrialsConfigContainer;

    private final CustomLogger LOGGER = CustomLogger.getInstance();


    private ConfigManager() {
        mainGson = new Gson();
        giantGson = new Gson();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public void loadSurvivalTrialsConfig() {
            Path configPath = FileAndDirectoryManager.getInstance().getModDirectory().resolve("survivalTrialsConfig.json");
            this.LOGGER.info(String.format("Loading Survival Trials Config from: %s", configPath.toString()));
            FileAndDirectoryManager.getInstance().logFileContents(configPath);
            loadConfig(configPath, SurvivalTrialsConfig.class, mainGson)
                    .ifPresent(config -> this.survivalTrialsConfigContainer = new SurvivalTrialsConfigContainer(config));

    }

    public void loadGearConfig() {

            Path gearConfigPath = FileAndDirectoryManager.getInstance().getModDirectory().resolve("gearConfig.json");
            this.LOGGER.info(String.format("Loading Gear Config from: %s", gearConfigPath.toString()));
            loadConfig(gearConfigPath, InitialGearConfigWrapper.class, mainGson)
                    .ifPresent(wrapper -> this.gearConfigContainer = new InitialGearConfigContainer(wrapper));

    }

    public void loadMobConfig() {

            Path mobConfigPath = FileAndDirectoryManager.getInstance().getModDirectory().resolve("mobOverrideConfig.json");
            this.LOGGER.info(String.format("Loading Mob Config from: %s", mobConfigPath.toString()));
            loadConfig(mobConfigPath, MobSpawnConfig.class, mainGson)
                    .ifPresent(config -> this.mobOverrideConfigContainer = new MobSpawnConfigContainer(config));

    }

    public void loadItemDropConfig() {

            Path itemDropConfigPath = FileAndDirectoryManager.getInstance().getModDirectory().resolve("itemDropOverrideConfig.json");
            this.LOGGER.info(String.format("Loading Item Drop Config from: %s", itemDropConfigPath.toString()));
            loadConfig(itemDropConfigPath, ItemDropConfigWrapper.class, mainGson)
                    .ifPresent(wrapper -> this.itemDropConfigContainer = new ItemDropConfigContainer(wrapper));

    }

    public void loadGiantConfig() {

            Path giantConfigPath = FileAndDirectoryManager.getInstance().getModDirectory().resolve("giantConfig.json");
            this.LOGGER.info(String.format("Loading Giant Config from: %s", giantConfigPath.toString()));
            loadConfig(giantConfigPath, GiantConfigWrapper.class, giantGson)
                    .ifPresent(wrapper -> this.giantConfigContainer = new GiantConfigContainer(wrapper));

    }



    public PlayerConfig getPlayerConfig(UUID playerUuid) {

            Path configPath = FileAndDirectoryManager.getInstance().getPlayerDataDirectory().resolve(playerUuid.toString() + ".json");
            this.LOGGER.info(String.format("Loading Player Config from: %s", configPath.toString()));
            if (Files.exists(configPath)) {
                try (Reader reader = Files.newBufferedReader(configPath)) {
                    this.LOGGER.info("Reading config file from: " + configPath.toAbsolutePath().toString());
                    return mainGson.fromJson(reader, PlayerConfig.class);
                } catch (IOException e) {
                    this.LOGGER.error(String.format("Failed to read player config - %s", e));
                }
            }
            return null;

    }

    public void savePlayerConfig(UUID playerUuid, PlayerConfig playerConfig) {
            Path configPath = FileAndDirectoryManager.getInstance().getPlayerDataDirectory().resolve(playerUuid.toString() + ".json");
            try (Writer writer = Files.newBufferedWriter(configPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                mainGson.toJson(playerConfig, writer);
                this.LOGGER.info("Writing config file to: " + configPath.toAbsolutePath().toString());
            } catch (IOException e) {
                this.LOGGER.error(String.format("Failed to write player config - %s", e));
            }
    }

    public boolean saveGiantConfig() {
        boolean didSave = false;

            // Serialize the updated configuration to JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonConfig = gson.toJson(this.giantConfigContainer.getGiantConfig());

            // Get the path to the configuration file
            FileAndDirectoryManager fileManager = FileAndDirectoryManager.getInstance();
            Path configFilePath = fileManager.getModDirectory().resolve("giantConfig.json");

            // Write the updated configuration to disk
            try (BufferedWriter writer = Files.newBufferedWriter(configFilePath)) {
                writer.write(jsonConfig);
                didSave = true;
            } catch (IOException e) {
                this.LOGGER.error(String.format("Failed to save Giant configuration - %s", e));
            }
        return didSave;
    }


    public boolean saveSurvivalTrialsConfig() {
        boolean didSave = false;
            // Serialize the updated configuration to JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonConfig = gson.toJson(this.survivalTrialsConfigContainer.getSurvivalTrialsConfig());
            // Get the path to the configuration file
            FileAndDirectoryManager fileManager = FileAndDirectoryManager.getInstance();
            Path configFilePath = fileManager.getModDirectory().resolve("survivalTrialsConfig.json");

            // Write the updated configuration to disk
            try (BufferedWriter writer = Files.newBufferedWriter(configFilePath)) {
                writer.write(jsonConfig);
                didSave = true;
            } catch (IOException e) {
                // Log the exception and notify command runner if necessary
                this.LOGGER.error(String.format("Failed to save configuration - %s", e));
                // The method to notify the command runner can be placed here
                // ModConfigCommand.notifyCommandRunner("The command was successful, but the config file has not been updated.");
            }
        return didSave;
    }


    private <T> Optional<T> loadConfig(Path path, Class<T> configClass, Gson gson) {

            try {
                if (!Files.exists(path)) {
                    this.LOGGER.error(String.format("Could not find %s", path));
                    return Optional.empty();
                }
                try (InputStream inputStream = Files.newInputStream(path)) {
                    try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                        T config = gson.fromJson(reader, configClass);
                        return Optional.of(config);
                    }
                }
            } catch (IOException e) {
                this.LOGGER.error(String.format("Failed to load %s - %s", path, e));
                return Optional.empty();
            }
    }

    /*********************************** Getters ****************************************/
    public InitialGearConfigContainer getInitialGearConfigContainer() {
        return gearConfigContainer;
    }

    public MobSpawnConfigContainer getMobSpawnConfigContainer() {
        return mobOverrideConfigContainer;
    }

    public ItemDropConfigContainer getItemDropConfigContainer() {
        return itemDropConfigContainer;
    }

    public SurvivalTrialsConfigContainer getSurvivalTrialsConfigContainer() {
        return survivalTrialsConfigContainer;
    }

    public GiantConfigContainer getGiantConfigContainer(){ return giantConfigContainer; }

    /********************************* HELPER FUNCTIONS *********************************************/
    public String toggleMainConfigOption(EnumTypes.ModConfigOption option) {
        SurvivalTrialsConfig.MainConfig config = this.getSurvivalTrialsConfigContainer().getSurvivalTrialsConfig().getSurvivalTrialsMainConfig();
        boolean newValue;
        switch (option) {
            case OVERRIDE_MOBS:
                newValue = !config.isOverrideMobs();
                config.setOverrideMobs(newValue);
                break;
            case SPAWN_GIANTS:
                newValue = !config.isSpawnGiants();
                config.setSpawnGiants(newValue);
                break;
            case GIVE_INITIAL_GEAR:
                newValue = !config.isGiveInitialGear();
                config.setGiveInitialGear(newValue);
                break;
            case GIVE_SPECIAL_LOOT:
                newValue = !config.isGiveInitialGear();
                config.setGiveSpecialLoot(newValue);
                break;
            case USE_PLAYER_HEADS:
                newValue = !config.isUsePlayerHeads();
                config.setUsePlayerHeads(newValue);
                break;
            case DEBUG_ON:
                newValue = !config.isDebugOn();
                config.setDebugOn(newValue);
                break;
            default:
                return null;
        }
        return "Toggled option " + option + " from " + !newValue + " to " + newValue;
    }

}

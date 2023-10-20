package com.dsd.ct.handlers;

import com.dsd.ct.CreatorTrials;
import com.dsd.ct.configs.GiantConfig;
import com.dsd.ct.configs.PlayerConfig;
import com.dsd.ct.configs.SurvivalTrialsConfig;
import com.dsd.ct.managers.ConfigManager;
import com.dsd.ct.managers.FileAndDirectoryManager;
import com.dsd.ct.managers.PlayerManager;
import com.dsd.ct.util.CustomLogger;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;


@Mod.EventBusSubscriber(modid = CreatorTrials.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEventHandler {
    private static final ConfigManager configManager = ConfigManager.getInstance();

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        for (PlayerConfig playerConfig : PlayerManager.getInstance().getAllPlayerConfigs().values()) {
            configManager.savePlayerConfig(playerConfig.getPlayerUuid(),playerConfig);
        }

        ConfigManager.getInstance().saveSurvivalTrialsConfig();

        CustomLogger.getInstance().outputBulkToConsole();
        CustomLogger.getInstance().outputtimerLogToConsole();
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        CustomLogger.getInstance().info(("****** Invoked the FMLDedicatedServerSetupEvent!! *********"));
        //CommandDispatcher<CommandSource> commandDispatcher = event.getServer().getCommands().getDispatcher();
       // ModConfigCommand.register(commandDispatcher);

        //-------COMMENT OUT BEFORE PUBLISHING--------------------------
        for (ServerLevel world : event.getServer().getAllLevels()) {
            // Set the time to midnight (18000 ticks)
            world.setDayTime(18000);
        }
        //-------------------------------------------------------------

    }

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        CustomLogger.getInstance().info(("Survival Trials Mod Server Start Method invoked"));
        Path serverDir = event.getServer().getWorldPath(LevelResource.ROOT);
        FileAndDirectoryManager.initialize(serverDir);

        CustomLogger.getInstance().info(String.format("Config Directory = %s",FileAndDirectoryManager.getInstance().getModDirectory().toString()));
        CustomLogger.getInstance().info(String.format("Player Directory = %s",FileAndDirectoryManager.getInstance().getPlayerDataDirectory().toString()));

        configManager.loadMobConfig();
        // Create Overridden Mobs using the loaded Mob Config

        configManager.loadSurvivalTrialsConfig();
        SurvivalTrialsConfig.MainConfig modConfig = ConfigManager.getInstance().getSurvivalTrialsConfigContainer().getSurvivalTrialsConfig().getSurvivalTrialsMainConfig();
        CustomLogger.getInstance().info(String.format("Loaded Main Mod Config = %s",modConfig.toString()));
        boolean isDebugOn = modConfig.isDebugOn();
        CustomLogger.getInstance().setDebugOn(isDebugOn);

        configManager.loadGearConfig();
        CustomLogger.getInstance().info(String.format("Initial Gear Config: %s", configManager.getInitialGearConfigContainer().getInitialGearConfig().toString()));

        configManager.loadItemDropConfig();
        CustomLogger.getInstance().info(String.format("Item Drop Config: %s", configManager.getItemDropConfigContainer().getItemDropConfig().toString()));

        configManager.loadGiantConfig();
        GiantConfig gcf = configManager.getGiantConfigContainer().getGiantConfig();
        CustomLogger.getInstance().debug(String.format("Giant Config: %s",gcf.toString()));



    }


}
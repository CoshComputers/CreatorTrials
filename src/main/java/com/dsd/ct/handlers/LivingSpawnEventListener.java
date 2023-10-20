package com.dsd.ct.handlers;

import com.dsd.ct.CreatorTrials;
import com.dsd.ct.configs.MobSpawnConfig;
import com.dsd.ct.configs.SurvivalTrialsConfig;
import com.dsd.ct.entities.CreatorTrialsBabyZombie;
import com.dsd.ct.managers.ConfigManager;
import com.dsd.ct.util.CustomLogger;
import com.dsd.ct.util.ModEnums;
import com.dsd.ct.util.ModUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreatorTrials.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingSpawnEventListener {
    private static int preventedHostileMobSpawns = 0;
    private static int unpreventedHostileMobSpawns = 0;

    @SubscribeEvent
    public static void onLivingSpawnEvent(MobSpawnEvent event) {

        //We only want to do this on the Server, not on the Client
        if(event.getLevel().isClientSide()){
            CustomLogger.getInstance().debug("Call was Client Side, doing nothing");
            return;
        }
        LivingEntity originalMob = event.getEntity();
        //CustomLogger.getInstance().debug(String.format("Living Spawn Event Called - Entity = %s",originalMob.toString()));
        //Prevent recursively calling this function if it's a baby zombie spawning, deny it.
        if(originalMob instanceof CreatorTrialsBabyZombie){
            return;
        }

        //We are only working with Monster Entities, and will treat the EnderDragon differently.
        if(originalMob instanceof Monster && !(originalMob instanceof EnderDragon)){

            SurvivalTrialsConfig.MainConfig mainConfig = ConfigManager.getInstance().getSurvivalTrialsConfigContainer()
                    .getSurvivalTrialsConfig().getSurvivalTrialsMainConfig();
            if (mainConfig.isOverrideMobs()) {
                event.setResult(Event.Result.DENY);
                preventedHostileMobSpawns++;

                if (event.isCancelable()) event.setCanceled(true);
                if(overRideGeneralMobSpawning(event)){

                    //Do Nothing
                }else{
                   //event.setResult(Event.Result.DENY);
                    //if (event.isCancelable()) event.setCanceled(true);
                    CustomLogger.getInstance().error("Something went wrong overriding the Mob Spawning");
                }
            }else{
                unpreventedHostileMobSpawns++;
                CustomLogger.getInstance().debug("Mob Spawning is not being overridden");

                // Clear the queue of pending hostile mob spawns
            }
            /*StringBuilder sb = new StringBuilder();
            sb.append("Prevented Hostile Mob Spawns = ").append(preventedHostileMobSpawns);
            sb.append("\tUnprevented Hostile Mob Spawns = ").append(unpreventedHostileMobSpawns);
            CustomLogger.getInstance().debug(sb.toString());*/
        }
    }
    private static boolean overRideGeneralMobSpawning(MobSpawnEvent event){
        MobSpawnConfig mobSpawnConfig = ConfigManager.getInstance().getMobSpawnConfigContainer().getMobSpawnConfig();
        boolean isSuccessful = false;
        LivingEntity originalMob = event.getEntity();
        ServerLevel level = (ServerLevel) event.getLevel();
        BlockPos pos = originalMob.blockPosition();
        int terrainHeight = level.getHeight(Heightmap.Types.WORLD_SURFACE,pos.getX(),pos.getZ());
        int blazeChance = mobSpawnConfig.getMobSpawnOverrides().get(0).getBlazeWeighting();
        int endermanChance = blazeChance + mobSpawnConfig.getMobSpawnOverrides().get(0).getEndermenWeighting();
        int randomNumber = ModUtilities.nextInt(100);
        CreatorTrialsBabyZombie newMob = null;

        newMob = new CreatorTrialsBabyZombie(EntityType.ZOMBIE,level);
        if(randomNumber < endermanChance){
            if(randomNumber<blazeChance) {
                newMob.setAppearance(ModEnums.SkullDropMapping.BLAZE);
            }else {
                newMob.setAppearance(ModEnums.SkullDropMapping.ENDERMAN);
            }
        }else{
            newMob.setAppearance(ModEnums.SkullDropMapping.UNKNOWN);
        }

        if(newMob != null){
            //Spawn the new Mob
            newMob.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,0,0);
            isSuccessful = level.addFreshEntity(newMob);
        }
        if(!isSuccessful){
           CustomLogger.getInstance().error(String.format("Failed to spawn overridden mob = %s",newMob.toString()));
        }
        return isSuccessful;
    }

}

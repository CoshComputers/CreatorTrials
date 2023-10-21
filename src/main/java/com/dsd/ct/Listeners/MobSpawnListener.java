package com.dsd.ct.Listeners;

import com.dsd.ct.entities.CreatorTrialsBabyZombie;
import com.dsd.ct.util.CustomLogger;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.living.MobSpawnEvent;

public class MobSpawnListener extends MobSpawnEvent {

    protected MobSpawnListener(Mob mob, ServerLevelAccessor level, double x, double y, double z) {
        super(mob, level, x, y, z);
        if (!(mob instanceof CreatorTrialsBabyZombie) || mob instanceof Monster) {
            CustomLogger.getInstance().debug(String.format("Not our Mob - Mob is [%s]",mob.getMobType()));
            this.setResult(Result.DENY);
        }else{
            CustomLogger.getInstance().debug(String.format("This is either passive or our mob"));
        }
    }

}

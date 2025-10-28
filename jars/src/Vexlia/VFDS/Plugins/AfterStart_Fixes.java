package Vexlia.VFDS.Plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.campaign.CharacterStats;
import com.fs.starfarer.campaign.rules.Memory;

import java.util.HashMap;

public class AfterStart_Fixes implements EconomyTickListener {

    public boolean devGameStart = false;

    private HashMap<String, MemoryAPI> memoryMap;



    @Override
    public void reportEconomyTick(int iterIndex) {}

    @Override
    public void reportEconomyMonthEnd() {

        MemoryAPI sector_mem = Global.getSector().getMemoryWithoutUpdate();

        if(sector_mem.getBoolean("$isVFDS")) devGameStart = true;

        Global.getSettings().setDevMode(true);

        if (devGameStart)
        {
            if(Global.getSector().getPlayerStats().getPoints() == 0 && Global.getSector().getPlayerStats().getSkillsCopy().isEmpty())
            {
                Global.getSector().getPlayerStats().addPoints(100);
            }
            devGameStart = false;
        }

        if (!devGameStart){
            Global.getSector().getListenerManager().removeListener(this);
        }
    }
}

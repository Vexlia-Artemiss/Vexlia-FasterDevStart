package Vexlia.VFDS.Plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.campaign.CharacterStats;

public class AfterStart_Fixes implements EconomyTickListener {

    public boolean devGameStart = true;

    @Override
    public void reportEconomyTick(int iterIndex) {}

    @Override
    public void reportEconomyMonthEnd() {
        if(!Global.getSettings().isDevMode()) devGameStart = false;

        if (devGameStart)
        {
            if(Global.getSector().getPlayerStats().getPoints() == 0
                    && Global.getSector().getPlayerStats().getSkillsCopy().isEmpty())
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

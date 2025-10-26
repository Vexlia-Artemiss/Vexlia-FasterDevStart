package Vexlia.VFDS.Plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;

import java.util.logging.Logger;

public class AfterStart_Fixes implements EconomyTickListener {

    private boolean gameStart = true;

    @Override
    public void reportEconomyTick(int iterIndex) {}

    @Override
    public void reportEconomyMonthEnd() {
        if (gameStart)
        {
            Global.getSector().getPlayerStats().addPoints(100);
            gameStart = false;
        }

        if (!gameStart){
            Global.getSector().getListenerManager().removeListener(this);
        }
    }
}

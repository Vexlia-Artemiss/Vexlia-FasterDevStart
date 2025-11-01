package Vexlia.VFDS;

import Vexlia.VFDS.Plugins.Vexlia_NewGameDialogPluginImpl;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import lunalib.lunaSettings.LunaSettings;
import org.apache.log4j.Priority;
import org.json.JSONException;

import java.io.IOException;
import java.util.logging.Logger;

public class VFDS_ModPlugin extends BaseModPlugin {

    public static boolean AUTO_DEVMODE = false;
    public static boolean SAFE_DEVMODE = true;

    private static boolean IS_VFDS = false;

    private boolean isActiveDevmode = false;

    public static org.apache.log4j.Logger log = Global.getLogger(VFDS_ModPlugin.class);

    public void onApplicationLoad() throws JSONException, IOException {

        LunaSettingsCheck();
    }

    //Help me

    @Override
    public void onNewGameAfterEconomyLoad() {
        super.onNewGameAfterEconomyLoad();

        IS_VFDS = Vexlia_NewGameDialogPluginImpl.isFVDS;

        if (IS_VFDS) {
            Global.getSector().getPlayerStats().addPoints(100);

            if(Global.getSector().getPlayerStats().getStoryPoints() < 1){
                Global.getSector().getPlayerStats().addStoryPoints(100);
            }
        }

        if (SAFE_DEVMODE || AUTO_DEVMODE) {
            Global.getSector().addScript(new NewGameDevmodeForcer());
        }
    }
    @Override
    public void beforeGameSave() {
        super.beforeGameSave();

        LunaSettingsCheck();

        if (Global.getSettings().isDevMode()) {
            isActiveDevmode = Global.getSettings().isDevMode();
        }

        if (SAFE_DEVMODE) {
            Global.getSettings().setDevMode(false);
        }
    }

    @Override
    public void onGameLoad(boolean newGame) {
        super.onGameLoad(newGame);

        LunaSettingsCheck();

        if (AUTO_DEVMODE){
            Global.getSettings().setDevMode(true);
        }
    }

    @Override
    public void afterGameSave() {
        super.afterGameSave();

        LunaSettingsCheck();

        if (SAFE_DEVMODE && isActiveDevmode) {
            Global.getSettings().setDevMode(true);
            isActiveDevmode = false;
        }
    }

    void LunaSettingsCheck(){
        if (Global.getSettings().getModManager().isModEnabled("lunalib")) {
            AUTO_DEVMODE = LunaSettings.getBoolean("Vexlia_FDS", "VFDS_auto_devmode");
            SAFE_DEVMODE = LunaSettings.getBoolean("Vexlia_FDS", "VFDS_safe_devmode");
        }
    }

    private class NewGameDevmodeForcer implements EveryFrameScript {

        boolean done = false;
        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public boolean runWhilePaused() {
            return true;
        }

        @Override
        public void advance(float amount) {
            if(!Global.getSettings().isDevMode() && AUTO_DEVMODE){
                Global.getSettings().setDevMode(true);
                done = true;

                log.log(Priority.INFO, "AUTO DEVMODE ACTIVATED");
            }
            if(!Global.getSettings().isDevMode() && isActiveDevmode) {
                Global.getSettings().setDevMode(true);
                done = true;

                log.log(Priority.INFO, "(SAFE DEVMODE) FIXED DEVMODE STATE AFTER FIRST SAVE");
            }

            Global.getSector().removeScript(this);
        }
    }
}

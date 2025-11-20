package Vexlia.VFDS;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import exerelin.ExerelinConstants;
import lunalib.lunaSettings.LunaSettings;
import org.apache.log4j.Priority;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class VFDS_ModPlugin extends BaseModPlugin {

    public static final String CONFIG_PATH = "VFDS_config.json";

    public static boolean autoDevmodeOnCampaign = false;
    public static boolean autoDevmodeOnAppStart = false;
    public static boolean safeDevmode = true;

    public static void loadSettings()
    {
        try
        {
            System.out.println("Loading exerelinSettings");

            JSONObject settings = Global.getSettings().getMergedJSONForMod(CONFIG_PATH, "Vexlia_FDS");

            autoDevmodeOnCampaign = settings.optBoolean("autoDevmodeOnCampaign", autoDevmodeOnCampaign);
            autoDevmodeOnAppStart = settings.optBoolean("autoDevmodeOnAppStart", autoDevmodeOnAppStart);

            safeDevmode = settings.optBoolean("safeDevmode", safeDevmode);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Failed to load config: " + e.getMessage(), e);
        }
    }



    private static boolean IS_VFDS = false;

    private boolean isActiveDevmode = false;

    public static org.apache.log4j.Logger log = Global.getLogger(VFDS_ModPlugin.class);

    public void onApplicationLoad() throws JSONException, IOException {
        loadSettings();
        LunaSettingsCheck();

        if (autoDevmodeOnAppStart) {
            Global.getSettings().setDevMode(true);
        }
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

        if (safeDevmode || autoDevmodeOnCampaign) {
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

        if (safeDevmode) {
            Global.getSettings().setDevMode(false);
        }
    }

    @Override
    public void onGameLoad(boolean newGame) {
        super.onGameLoad(newGame);

        LunaSettingsCheck();

        if (autoDevmodeOnCampaign){
            Global.getSettings().setDevMode(true);
        }
    }

    @Override
    public void afterGameSave() {
        super.afterGameSave();

        LunaSettingsCheck();

        if (safeDevmode && isActiveDevmode) {
            Global.getSettings().setDevMode(true);
            isActiveDevmode = false;
        }
    }

    void LunaSettingsCheck(){
        if (Global.getSettings().getModManager().isModEnabled("lunalib")) {
            autoDevmodeOnCampaign = LunaSettings.getBoolean("Vexlia_FDS", "VFDS_auto_devmode");
            autoDevmodeOnAppStart = LunaSettings.getBoolean("Vexlia_FDS", "VFDS_devmode_onappstart");

            safeDevmode = LunaSettings.getBoolean("Vexlia_FDS", "VFDS_safe_devmode");
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
            if(!Global.getSettings().isDevMode() && autoDevmodeOnCampaign){
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

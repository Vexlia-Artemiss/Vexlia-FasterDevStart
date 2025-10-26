package Vexlia.VFDS;

import Vexlia.VFDS.Plugins.AfterStart_Fixes;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

public class VFDS_ModPlugin extends BaseModPlugin {

    @Override
    public void onNewGameAfterEconomyLoad() {
        super.onNewGameAfterEconomyLoad();

        Global.getSector().getListenerManager().addListener(new AfterStart_Fixes());
    }
}

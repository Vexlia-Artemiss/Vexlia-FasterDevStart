package com.fs.starfarer.api.impl.campaign.rulecmd.newgame;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import exerelin.utilities.NexUtilsFaction;
import exerelin.utilities.NexUtilsGUI;
import exerelin.utilities.StringHelper;
import exerelin.world.factionsetup.FactionSetupHandler;
import exerelin.world.factionsetup.FactionSetupItemPlugin;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Nex_NGCarePackage extends BaseCommandPlugin {

    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        if (dialog == null) return false;

        //Rules studd for non nexerelin to work
        return true;
    }
}

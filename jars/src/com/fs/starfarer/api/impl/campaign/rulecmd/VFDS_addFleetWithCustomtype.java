package com.fs.starfarer.api.impl.campaign.rulecmd;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.newgame.NGCAddStartingShipsByFleetType;
import com.fs.starfarer.api.util.Misc;
import lunalib.lunaSettings.LunaSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VFDS_addFleetWithCustomtype extends BaseCommandPlugin{
    public static InteractionDialogAPI CurrentDialogue;

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        String fleettype = LunaSettings.getString("Vexlia_FDS", "VFDS_CustomisedStart_Fleet");

        Misc.Token token = new Misc.Token(fleettype, Misc.TokenType.LITERAL);
        params.add(0, token);

        new NGCAddStartingShipsByFleetType().execute(ruleId, dialog, params, memoryMap);
        return false;
    }
}

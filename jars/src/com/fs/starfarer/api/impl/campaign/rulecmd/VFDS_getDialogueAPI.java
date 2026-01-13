package com.fs.starfarer.api.impl.campaign.rulecmd;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.newgame.NGCAddStartingShipsByFleetType;
import com.fs.starfarer.api.util.Misc;

import java.util.List;
import java.util.Map;

public class VFDS_getDialogueAPI extends BaseCommandPlugin{
    public static InteractionDialogAPI CurrentDialogue;

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        CurrentDialogue = dialog;

        //Global.getSector().getMemoryWithoutUpdate().set("$VFDS_FleetType", "EXPLORER_LARGE");
        return false;
    }
}

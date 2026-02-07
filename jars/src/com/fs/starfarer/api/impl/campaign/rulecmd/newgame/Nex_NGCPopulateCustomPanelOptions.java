package com.fs.starfarer.api.impl.campaign.rulecmd.newgame;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.CharacterCreationData;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.impl.campaign.rulecmd.Nex_VisualCustomPanel;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI.TooltipCreator;
import com.fs.starfarer.api.util.Misc;
import exerelin.campaign.ExerelinSetupData;
import exerelin.campaign.ExerelinSetupData.HomeworldPickMode;
import exerelin.campaign.ExerelinSetupData.StartRelationsMode;
import exerelin.campaign.RevengeanceManager;
import exerelin.campaign.ui.InteractionDialogCustomPanelPlugin;
import exerelin.utilities.NexConfig;
import exerelin.utilities.NexUtilsGUI;
import exerelin.utilities.NexUtilsGUI.CustomPanelGenResult;
import exerelin.utilities.StringHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static exerelin.campaign.ui.CustomPanelPluginWithInput.ButtonEntry;
import static exerelin.campaign.ui.CustomPanelPluginWithInput.RadioButtonEntry;

public class Nex_NGCPopulateCustomPanelOptions extends BaseCommandPlugin {

	@Override
	public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
		if (dialog == null) return false;

		//Rules studd for non nexerelin to work
		return true;
	}
}

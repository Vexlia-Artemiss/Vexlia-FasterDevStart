package Vexlia.VFDS.Plugins;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.CharacterCreationData;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.impl.SharedSettings;
import com.fs.starfarer.api.impl.campaign.DevMenuOptions;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.rulecmd.DumpMemory;
import com.fs.starfarer.api.impl.campaign.rulecmd.FireAll;
import com.fs.starfarer.api.impl.campaign.rulecmd.FireBest;

import Vexlia.VFDS.Plugins.AfterStart_Fixes;

public class Vexlia_NewGameDialogPluginImpl implements InteractionDialogPlugin {

    public static String CAMPAIGN_HELP_POPUPS_OPTION_CHECKED = "campaignHelpPopupsOptionChecked";

    private static enum OptionId {
        INIT,
        CONTINUE_CHOICES,
        DEVMODE_FAST_START,
        DEVMODE_FAST_START_NO_TIME_SKIP,
        NEX_FAST_START,
        LEAVE,
    }

    private static enum State {
        OPTIONS,
        CHOICES,
    }

    private InteractionDialogAPI dialog;
    private TextPanelAPI textPanel;
    private OptionPanelAPI options;
    private VisualPanelAPI visual;

    private CharacterCreationData data;
    private SectorEntityToken entity;

    private State state = State.OPTIONS;
    private HashMap<String, MemoryAPI> memoryMap;
    private MemoryAPI memory;

    public void init(InteractionDialogAPI dialog) {
        this.dialog = dialog;
        textPanel = dialog.getTextPanel();
        options = dialog.getOptionPanel();
        visual = dialog.getVisualPanel();

        dialog.setOptionOnEscape("Leave", OptionId.LEAVE);
        createInitialOptions();

        entity = dialog.getInteractionTarget();
        memory = entity.getMemoryWithoutUpdate();
        data = (CharacterCreationData) memory.get("$characterData");

        memoryMap = new HashMap<String, MemoryAPI>();
        memoryMap.put(MemKeys.LOCAL, memory);
        memoryMap.put(MemKeys.GLOBAL, Global.getFactory().createMemory());
        if (Global.getSettings().isDevMode()) {
            memoryMap.get(MemKeys.GLOBAL).set("$isDevMode", true, 0);
        }

        dialog.setPromptText("-");

        data.setCampaignHelpEnabled(SharedSettings.optBoolean(CAMPAIGN_HELP_POPUPS_OPTION_CHECKED, true));

        dialog.hideTextPanel();
        visual.showNewGameOptionsPanel(data);
    }

    public void advance(float amount) {

        if (data.getCharacterData().getName() == null || data.getCharacterData().getName().isEmpty()) {
            FullName RandomName = new FullName("DevMode", "FastStart", FullName.Gender.ANY);

            if(Global.getSettings().getModManager().isModEnabled("nexerelin"))
                RandomName = new FullName("DevMode", "NexFastStart", FullName.Gender.ANY);

            data.getPerson().setName(RandomName);
            data.getCharacterData().setName(RandomName.getFullName(), RandomName.getGender());
        }

        if(Global.getSettings().isDevMode()) {
            Global.getSettings().setDevMode(false);
        }

        if (state == State.OPTIONS) {
            String name = data.getCharacterData().getName();
            if (name == null || name.isEmpty()) {
                options.setEnabled(OptionId.CONTINUE_CHOICES, false);
            } else {
                options.setEnabled(OptionId.CONTINUE_CHOICES, true);
            }
        } else if (state == State.CHOICES) {

            if (data.isDone()) {
                dialog.dismiss();
            }
        }
    }

    public Map<String, MemoryAPI> getMemoryMap() {
        return memoryMap;
    }

    public void backFromEngagement(EngagementResultAPI result) {
        // no combat here, so this won't get called
    }

    public void optionSelected(String text, Object optionData) {

        if (optionData == null) return;

        if (text != null && state == State.CHOICES) {
            //textPanel.addParagraph(text, Global.getSettings().getColor("buttonText"));
            dialog.addOptionSelectedText(optionData);
        }

        if (optionData instanceof String) {
            if (optionData == DumpMemory.OPTION_ID) {
                new DumpMemory().execute(null, dialog, null, memoryMap);
                return;
            } else if (DevMenuOptions.isDevOption(optionData)) {
                DevMenuOptions.execute(dialog, (String) optionData);
                return;
            }

            memory.set("$option", optionData);
            memory.expire("$option", 0);
            fireBest("NewGameOptionSelected");
        } else {
            OptionId option = (OptionId) optionData;
            switch (option) {
                case LEAVE:
                    SharedSettings.setBoolean(CAMPAIGN_HELP_POPUPS_OPTION_CHECKED, data.isCampaignHelpEnabled());
                    SharedSettings.saveIfNeeded();
                    dialog.dismissAsCancel();
                    break;
                case CONTINUE_CHOICES:
                    SharedSettings.setBoolean(CAMPAIGN_HELP_POPUPS_OPTION_CHECKED, data.isCampaignHelpEnabled());
                    SharedSettings.saveIfNeeded();

                    dialog.showTextPanel();
                    visual.showPersonInfo(data.getPerson(), true);
                    options.clearOptions();
                    state = State.CHOICES;
                    fireBest("BeginNewGameCreation");
                    break;
                case DEVMODE_FAST_START:
                    SharedSettings.setBoolean(CAMPAIGN_HELP_POPUPS_OPTION_CHECKED, data.isCampaignHelpEnabled());
                    SharedSettings.saveIfNeeded();

                    dialog.showTextPanel();
                    visual.showPersonInfo(data.getPerson(), true);
                    options.clearOptions();
                    state = State.CHOICES;
                    fireBest("VFDS_DevStart_Trigger");
                    break;
                case DEVMODE_FAST_START_NO_TIME_SKIP:
                    SharedSettings.setBoolean(CAMPAIGN_HELP_POPUPS_OPTION_CHECKED, data.isCampaignHelpEnabled());
                    SharedSettings.saveIfNeeded();


                    dialog.showTextPanel();
                    visual.showPersonInfo(data.getPerson(), true);
                    options.clearOptions();
                    state = State.CHOICES;
                    fireBest("VFDS_DevStart_NoTimeSkip_Trigger");
                    break;
                case NEX_FAST_START:
                    SharedSettings.setBoolean(CAMPAIGN_HELP_POPUPS_OPTION_CHECKED, data.isCampaignHelpEnabled());
                    SharedSettings.saveIfNeeded();

                    dialog.showTextPanel();
                    visual.showPersonInfo(data.getPerson(), true);
                    options.clearOptions();
                    state = State.CHOICES;
                    //memoryMap.get(MemKeys.GLOBAL).set("$isVFDS", true);
                    fireBest("VFDS_nex_DevStart_Trigger");

                    break;

            }
        }
    }

    private void createInitialOptions() {
        options.clearOptions();
        boolean dev = Global.getSettings().isDevMode();
        options.addOption("Continue", OptionId.CONTINUE_CHOICES, null);

        if (!Global.getSettings().getModManager().isModEnabled("nexerelin")) {
            options.addOption("(VFDS) Fast Start", OptionId.DEVMODE_FAST_START, null);
            options.addOption("(VFDS) Fast Start (No time skip)", OptionId.DEVMODE_FAST_START_NO_TIME_SKIP, null);
        }
        else if (Global.getSettings().getModManager().isModEnabled("nexerelin")) {
            options.addOption("(VFDS) Nexerelin Fast Start", OptionId.NEX_FAST_START);
        }

        if(dev) {

        }
        options.addOption("Leave", OptionId.LEAVE, null);
    }

    private OptionId lastOptionMousedOver = null;
    public void optionMousedOver(String optionText, Object optionData) {
    }

    public Object getContext() {
        return null;
    }

    public boolean fireAll(String trigger) {
        return FireAll.fire(null, dialog, memoryMap, trigger);
    }

    public boolean fireBest(String trigger) {
        return FireBest.fire(null, dialog, memoryMap, trigger);
    }
}
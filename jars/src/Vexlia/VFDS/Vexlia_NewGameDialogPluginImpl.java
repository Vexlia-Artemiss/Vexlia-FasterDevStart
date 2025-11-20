package Vexlia.VFDS;

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
import com.fs.starfarer.api.impl.campaign.rulecmd.DumpMemory;
import com.fs.starfarer.api.impl.campaign.rulecmd.FireAll;
import com.fs.starfarer.api.impl.campaign.rulecmd.FireBest;
import lunalib.lunaSettings.LunaSettings;

public class Vexlia_NewGameDialogPluginImpl implements InteractionDialogPlugin {

    String FirstName = "DevMode";

    String LastName = "FastStart";

    public static String CAMPAIGN_HELP_POPUPS_OPTION_CHECKED = "campaignHelpPopupsOptionChecked";

    public static boolean isFVDS = false;

    private static enum OptionId {
        INIT,
        CONTINUE_CHOICES,
        DEVMODE_FAST_START,
        DEVMODE_FAST_START_WITH_TIME_PASS,

        NEX_FAST_START,
        NEX_FAST_START_ALT,
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

            if (Global.getSettings().getModManager().isModEnabled("lunalib")) {
                FirstName = LunaSettings.getString("Vexlia_FDS", "VFDS_FirstName");
                LastName = LunaSettings.getString("Vexlia_FDS", "VFDS_LastName");
            }

            if(Global.getSettings().getModManager().isModEnabled("nexerelin")) {

                FirstName = "DevMode";
                LastName = "NexFastStart";

                if (Global.getSettings().getModManager().isModEnabled("lunalib")) {
                    FirstName = LunaSettings.getString("Vexlia_FDS", "VFDS_NexerelinFirstName");
                    LastName = LunaSettings.getString("Vexlia_FDS", "VFDS_NexerelinLastName");
                }
            }

            FullName RandomName = new FullName(FirstName, LastName, FullName.Gender.ANY);

            data.getPerson().setName(RandomName);
            data.getCharacterData().setName(RandomName.getFullName(), RandomName.getGender());
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
                    isFVDS = false;
                    dialog.dismissAsCancel();

                    break;
                case CONTINUE_CHOICES:
                    SharedSettings.setBoolean(CAMPAIGN_HELP_POPUPS_OPTION_CHECKED, data.isCampaignHelpEnabled());
                    SharedSettings.saveIfNeeded();

                    dialog.showTextPanel();
                    visual.showPersonInfo(data.getPerson(), true);
                    options.clearOptions();
                    state = State.CHOICES;

                    isFVDS = false;
                    fireBest("BeginNewGameCreation");

                    break;
                case DEVMODE_FAST_START:
                    SharedSettings.saveIfNeeded();

                    dialog.showTextPanel();
                    visual.showPersonInfo(data.getPerson(), true);
                    options.clearOptions();
                    state = State.CHOICES;

                    isFVDS = true;
                    fireBest("VFDS_DevStart_Trigger");

                    break;
                case DEVMODE_FAST_START_WITH_TIME_PASS:
                    SharedSettings.saveIfNeeded();

                    dialog.showTextPanel();
                    visual.showPersonInfo(data.getPerson(), true);
                    options.clearOptions();
                    state = State.CHOICES;

                    isFVDS = true;
                    fireBest("VFDS_DevStart_NoTimeSkip_Trigger");

                    break;
                case NEX_FAST_START:
                    SharedSettings.saveIfNeeded();

                    dialog.showTextPanel();
                    visual.showPersonInfo(data.getPerson(), true);
                    options.clearOptions();
                    state = State.CHOICES;

                    isFVDS = true;
                    fireBest("VFDS_nex_DevStart_Trigger");

                    break;
                case NEX_FAST_START_ALT:
                    SharedSettings.saveIfNeeded();

                    dialog.showTextPanel();
                    visual.showPersonInfo(data.getPerson(), true);
                    options.clearOptions();
                    state = State.CHOICES;

                    isFVDS = true;
                    fireBest("VFDS_nex_DevStart_Alt_Trigger");

                    break;
            }
        }
    }

    private void createInitialOptions() {
        options.clearOptions();
        boolean dev = Global.getSettings().isDevMode();
        options.addOption("Continue", OptionId.CONTINUE_CHOICES, null);

        if (!Global.getSettings().getModManager().isModEnabled("nexerelin")) {
            options.addOption("(VFDS) Fast Start (No time pass)", OptionId.DEVMODE_FAST_START, null);
            options.setTooltip(OptionId.DEVMODE_FAST_START, "Normal vanilla Devmode Fast Start. Doesn't simulate 2 months.");
            options.setTooltipHighlights(OptionId.DEVMODE_FAST_START, "Doesn't simulate 2 months.");


            options.addOption("(VFDS) Fast Start (With time pass)", OptionId.DEVMODE_FAST_START_WITH_TIME_PASS, null);
            options.setTooltip(OptionId.DEVMODE_FAST_START_WITH_TIME_PASS, "Normal vanilla Devmode Fast Start. Will simulate 2 months as normal game load.");
            options.setTooltipHighlights(OptionId.DEVMODE_FAST_START_WITH_TIME_PASS, "Will simulate 2 months as normal game load.");
        }
        else if (Global.getSettings().getModManager().isModEnabled("nexerelin")) {
            options.addOption("(VFDS) Nexerelin Fast Start (No time pass)", OptionId.NEX_FAST_START);
            options.setTooltip(OptionId.NEX_FAST_START, "Recreated normal Nexerelin Fast Start. In most cases will share problems with build-in Nexerelin Fast Start");

            options.addOption("(VFDS) Nexerelin Alt Fast Start (No time pass)", OptionId.NEX_FAST_START_ALT);
            options.setTooltip(OptionId.NEX_FAST_START_ALT, "Alternative to typical Nexerelin Fast Start. Should be less prone to crashing.");
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
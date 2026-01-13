package Vexlia.VFDS;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.FireBest;
import com.fs.starfarer.api.impl.campaign.rulecmd.VFDS_getDialogueAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.newgame.NGCAddStartingShipsByFleetType;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.Misc;
import lunalib.lunaSettings.LunaSettings;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VFDS_ButtonPresserScript implements EveryFrameCombatPlugin {

    private final int[] numberIds = {KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9};

    Logger log = Global.getLogger(VFDS_ButtonPresserScript.class);

    float timer;

    int i = 0;

    boolean sequenceSwitch = true;
    public boolean runOnce = true;
    boolean customisedStart = Vexlia_NewGameDialogPluginImpl.customisedStart;

    String baseString = null;

    @Override
    public void processInputPreCoreControls(float amount, List<InputEventAPI> events) {

        customisedStart = Vexlia_NewGameDialogPluginImpl.customisedStart;
        baseString = LunaSettings.getString("Vexlia_FDS", "VFDS_CustomisedStartOrder");
        String stringArray[] = baseString.split("");
        float delay = LunaSettings.getDouble("Vexlia_FDS", "VFDS_CustomisedStart_Delay").floatValue();

        //When script idle - before start chosen
        if(!customisedStart) {
            //Update strings
            i = 0;
            runOnce = true;
        }



        if (customisedStart) {

            if (i >= stringArray.length) {
                i = 0;
                if (dialog != null) {
                    fireBest("VFDS_SequenceFinished_Trigger");
                }

                runOnce = false;
            }
            if (!runOnce) {
                return;
            }

            timer += amount;
            if (timer <= delay) {
                dialog = VFDS_getDialogueAPI.CurrentDialogue;
                return;
            }



            log.log(Priority.INFO, "Tried to press button or fire rule");
            switch (stringArray[i]) {
                case "G" -> {
                    try {
                        Robot robot = new Robot();
                        if (customisedStart) {
                            {
                                robot.keyPress(KeyEvent.VK_G);
                                robot.keyRelease(KeyEvent.VK_G);
                                //robot.keyPress(numberIds[(int) "1"]);
                                log.log(Priority.INFO, "Pressed button:" + stringArray[i]);
                            }
                            customisedStart = false;
                        }
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                case "E" -> {
                    try {
                        Robot robot = new Robot();
                        if (customisedStart) {
                            {
                                robot.keyPress(KeyEvent.VK_ESCAPE);
                                robot.keyRelease(KeyEvent.VK_ESCAPE);
                                //robot.keyPress(numberIds[(int) "1"]);
                                log.log(Priority.INFO, "Pressed button:" + stringArray[i]);
                            }
                            customisedStart = false;
                        }
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                case "F" -> {
                    try {
                        Robot robot = new Robot();
                        if (customisedStart) {
                            {
                                dialog = VFDS_getDialogueAPI.CurrentDialogue;

                                fireBest("VFDS_Customised_Start_Trigger");
                                log.log(Priority.INFO, "Tried to fire rule");
                            }
                            customisedStart = false;
                        }
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                default -> {
                    try {
                        Robot robot = new Robot();
                        if (customisedStart) {
                            {
                                robot.keyPress(numberIds[Integer.parseInt(stringArray[i])]);
                                robot.keyRelease(numberIds[Integer.parseInt(stringArray[i])]);
                                //robot.keyPress(numberIds[(int) "1"]);
                                log.log(Priority.INFO, "Pressed button:" + stringArray[i]);
                            }
                            customisedStart = false;
                        }
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }

            timer = 0;

            if(i < stringArray.length) {
                if (stringArray[i].equals("G")) {
                    timer = -0.5f;
                }
            }
        }
    }

    private InteractionDialogAPI dialog;
    private HashMap<String, MemoryAPI> memoryMap = new HashMap<String, MemoryAPI>();
    private MemoryAPI memory;
    private SectorEntityToken entity;

    public boolean fireBest(String trigger) {
        entity = dialog.getInteractionTarget();
        memory = entity.getMemoryWithoutUpdate();
        memoryMap.put(MemKeys.LOCAL, memory);
        memoryMap.put(MemKeys.GLOBAL, Global.getFactory().createMemory());

        return FireBest.fire(null, dialog, memoryMap, trigger);
    }

    @Override
    public void init(CombatEngineAPI engine) {}
    @Override
    public void advance(float amount, List<InputEventAPI> events) {}
    @Override
    public void renderInWorldCoords(ViewportAPI viewport) {}
    @Override
    public void renderInUICoords(ViewportAPI viewport) {}
}

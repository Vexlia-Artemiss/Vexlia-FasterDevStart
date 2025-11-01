# Vexlia's Fast DevStart

Small QoL utility mod to add fast start options right on the new game creation screen and get to sector generation in one click. Created mostly for modders who need to reload the game a lot.

Should be safe to remove at any point.

What mod does precisely?

- Autosets the player name as "DevMode FastStart" (Or "Devmode NexFastStart" if Nexerelin is active). This also allows you to click `Continue` right away without inputting the name.
Note: UI panel doesn't update, but name is set.
- Adds option on first screen to choice "Fast start", skipping pressing `Continue`.
- Choosing fast start skips skill point distribution screen, and gives skill points after, if you still need them for testing.
- Fast Start is equivalent to devmode start in stats.
- Adds similar fast start for Nexerelin (and use its devmode start values).

Also supports few additional features changeable with LunaLib:

Safe Devmode enables devmode switching off before saving. Devmode is reenabled after save, so in most cases it should be seamless. [Should prevent crashes for mods which can't create saves with active devmode.]

Auto Devmode activates devmode after game creation or load. [Independent of start chosen and save loaded.]

Note: "Safe Devmode" is enabled without active Lunalib, and "Auto devmode" disabled without active Lunalib

Ability to change strings for autoset player name (separate ones for if Nexerelin is active or not).

## Credits:
Vexlia Artemiss for being me and also mod author.

Alex for the original "NewGameDialogPluginImpl" code which my mod builds off and replaces.

Nexerelin authors for rulesCMD code I used to facilitate Nexerelin integration.

Bugatti Echelon from Discord for testing and finding bugs. And for being the main inspiration for part of the features.

# Vexlia-FasterDevStart

Shortens and simplifies Devstart for modders who need to reload the game a lot. 

Mod autosets the player name as "DevMode Faster", it just UI panels don't update. You can overwrite the name, as it is set only if the current name is empty. This also allows you to click `Continue` right away. Also skips skill point distribution screen, and gives skill points after start, if you still need them for testing. 

**Changes and options only appear if devmod is active.**

**ATTENTION: MOD ONLY WORKS IF YOU SWAP VANILLA PLUGIN WITH MINE IN:**
`\starsector-core\data\config\setting.json`

For best experience use lines from bellow:
```
#"newGameDialogPlugin":"com.fs.starfarer.api.impl.campaign.NewGameDialogPluginImpl",
"newGameDialogPlugin":"Vexlia.VFDS.Plugins.Vexlia_NewGameDialogPluginImpl",
```
Doesn’t fully work with Nexerelin (same as vanilla devstart), but allows you to skip clicking Random to get a name. For safety, I disable mod's devstart options if nexerelin present. Mod is utility but to fully remove it you need to revert setting changes.

# MC Battleship
A simple Spigot plugin that allows you to play the classic Battleship game in Minecraft. Currently, singleplayer against computer only.

## Installation
1. Download the latest Spigot 1.19.2 release from the [Spigot releases page](https://getbukkit.org/download/spigot).
2. Put the downloaded jar file in an empty folder.
3. Run the jar. You can do it by running `java -jar spigot-1.19.2.jar` in the folder where the jar is located.
4. The server will stop after a few seconds. This is normal. You can now close the terminal.
5. Open the eula file in the folder where the jar is located. Change `eula=false` to `eula=true`, if you accept the Minecraft EULA.
6. Download the latest MC Battleship release from the [releases page](https://github.com/PaueteKKUET/MCBattleship/releases).
7. Put the downloaded jar file in the plugins folder that was created when you ran the Spigot jar.
8. Run the Spigot jar again.
9. The server should now be running. You can connect to it by typing `localhost` in the Minecraft client.

## Usage
In order to play the game, you must configure the game first. The `/setup` command will configure all we need for the current world. It takes some args:

- `radar [x y z]`: The coordinates of the radar origin block. The radar will be placed in a 10x1x10 area at the south east from this block.
- `canon [x y z]`: The coordinates of the canon center block. The canon will create a beacon at that block on fire.
- `ships [x y z]`: The coordinates of the ship map origin block. Ally ships will be placed in a 30x3x30 area at the south east from this block.

An example of the setup command is `/setup 808 232 -3685 813 247 -3708 832 208 -3702`. The game will be configured for the current world. You can use the `/setup` command again to change the configuration.

After the setup, you can start the game by typing `/start`. You can stop the game by typing `/leave`.

Try to sink all the enemy ships before they sink yours!

## Example map
You can download an example map from the [releases page](https://github.com/PaueteKKUET/MCBattleship/releases). It contains created environment. You can use it to play the game without having to create a map.

Unzip the downloaded file and put the `world` folder in the `server` folder (the server can't be running). Then, run the Spigot jar and connect to the server by typing `localhost` in the Minecraft client.

After joining the server, type the command `/setup 808 232 -3685 813 247 -3708 832 208 -3702` to configure the game. Then, type `/start` to start the game.
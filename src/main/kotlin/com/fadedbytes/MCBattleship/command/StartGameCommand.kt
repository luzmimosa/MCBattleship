package com.fadedbytes.MCBattleship.command

import com.fadedbytes.MCBattleship.BattleshipPlugin
import com.fadedbytes.MCBattleship.mcgame.MinecraftGame
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StartGameCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command")
            return true
        }

        try {

            MinecraftGame.worldInfoManager.getInfo(sender.location.world!!)?.let {

                BattleshipPlugin.activeGames.add(
                    MinecraftGame(
                        sender,
                        10,
                        it.radarLocation.block,
                        it.canonLocation.block,
                        it.shipboardLocation.block
                    ) {game ->
                        BattleshipPlugin.activeGames.remove(game)
                    }
                )

                sender.sendMessage("${ChatColor.GREEN}Game started")
            } ?: run {
                sender.sendMessage("${ChatColor.RED}No game setup for this world")
            }
        } catch (e: IllegalStateException) {
            sender.sendMessage("Ya estás jugando :(")
        }

        return true
    }

}
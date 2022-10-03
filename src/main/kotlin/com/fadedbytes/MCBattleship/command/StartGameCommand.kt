package com.fadedbytes.MCBattleship.command

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
            TODO("Start game")

            sender.sendMessage("${ChatColor.GOLD}Starting game for you :)")
        } catch (e: IllegalStateException) {
            sender.sendMessage("Ya estás jugando :(")
        }

        return true
    }

}
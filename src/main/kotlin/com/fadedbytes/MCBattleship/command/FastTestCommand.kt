package com.fadedbytes.MCBattleship.command

import com.fadedbytes.MCBattleship.mcgame.MinecraftGame
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FastTestCommand: CommandExecutor {

    companion object {

    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command")
            return true
        }

        try {
            val radar = sender.location.world?.getBlockAt(args[0].toInt(), args[1].toInt(), args[2].toInt()) !!
            val canon = sender.location.world?.getBlockAt(args[3].toInt(), args[4].toInt(), args[5].toInt()) !!
            val shipboard = sender.location.world?.getBlockAt(args[6].toInt(), args[7].toInt(), args[8].toInt()) !!

            val game = MinecraftGame(
                sender,
                10,
                radar,
                canon,
                shipboard
            )
        } catch (e: Exception) {
            sender.sendMessage("${ChatColor.RED}Invalid arguments")
        }



        return true
    }

}
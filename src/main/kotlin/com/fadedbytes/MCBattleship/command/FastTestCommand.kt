package com.fadedbytes.MCBattleship.command

import com.fadedbytes.MCBattleship.mcgame.MinecraftGame
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FastTestCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command")
            return true
        }

        if (MinecraftGame.isPlaying(sender)) {
            sender.sendMessage("Ya estás jugando :(")
            return true
        }

        MinecraftGame(sender, sender.location, 10)


        return true
    }

}
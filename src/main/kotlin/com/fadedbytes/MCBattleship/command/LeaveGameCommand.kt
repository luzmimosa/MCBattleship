package com.fadedbytes.MCBattleship.command

import com.fadedbytes.MCBattleship.mcgame.MinecraftGame
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LeaveGameCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command")
            return true
        }

        if (MinecraftGame.isPlaying(sender)) {
            MinecraftGame.getGame(sender)?.endGame()
            sender.sendMessage("Game ended")
        } else {
            sender.sendMessage("You are not playing")
        }

        return true

    }
}
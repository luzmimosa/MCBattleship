package com.fadedbytes.MCBattleship.command

import com.fadedbytes.MCBattleship.game.GameMaster
import com.fadedbytes.MCBattleship.worldboard.MinecraftGame
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

        if (GameMaster.isPlaying(sender)) {

            val game = GameMaster.getLogicalGame(sender) ?: return true

            val randomX = game.redPlayer.getRandomCoordinates().first
            val randomY = game.redPlayer.getRandomCoordinates().second

            game.redPlayer.fire(randomX, randomY)

            return true
        }

        val game: MinecraftGame = GameMaster.startGameFor(sender)
        sender.sendMessage("UwU")

        return true
    }

}
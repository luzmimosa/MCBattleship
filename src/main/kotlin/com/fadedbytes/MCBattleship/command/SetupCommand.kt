package com.fadedbytes.MCBattleship.command

import com.fadedbytes.MCBattleship.mcgame.MinecraftGame
import com.fadedbytes.MCBattleship.mcgame.PersistentGameInfo
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetupCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("This command can only be executed by a player")
            return true
        }

        if (args.size != 9) {
            sender.sendMessage("Invalid arguments")
            return true
        }

        try {
            val radar = sender.location.world?.getBlockAt(args[0].toInt(), args[1].toInt(), args[2].toInt())!!
            val canon = sender.location.world?.getBlockAt(args[3].toInt(), args[4].toInt(), args[5].toInt())!!
            val shipboard = sender.location.world?.getBlockAt(args[6].toInt(), args[7].toInt(), args[8].toInt())!!

            MinecraftGame.worldInfoManager.setOrUpdateInfo(
                PersistentGameInfo(
                    radar.location,
                    canon.location,
                    shipboard.location,
                    sender.location.world!!
                )
            )

            sender.sendMessage("Setup complete for world ${sender.location.world?.name}")

        } catch (e: Exception) {
            sender.sendMessage("Failed to setup game. See console for details")
            e.printStackTrace()
        }

        return true

    }
}
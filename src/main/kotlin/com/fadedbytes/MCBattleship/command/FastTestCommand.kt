package com.fadedbytes.MCBattleship.command

import com.fadedbytes.MCBattleship.mcgame.features.canon.FroglightCanon
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FastTestCommand: CommandExecutor {

    companion object {
        var canon: FroglightCanon? = null
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command")
            return true
        }

        if (canon == null) {
            canon = FroglightCanon(sender.location)
        } else {
            canon?.prepareShoot(sender.location)
        }

        return true
    }

}
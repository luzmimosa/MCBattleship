package com.fadedbytes.MCBattleship.command

import com.fadedbytes.MCBattleship.game.GameBoard
import com.fadedbytes.MCBattleship.game.Ship
import com.fadedbytes.MCBattleship.game.ShipInfo
import com.fadedbytes.MCBattleship.worldboard.WorldBoard
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

        val gameBoard = GameBoard(10)

        gameBoard.addShip(Ship.CARRIER, ShipInfo(0, 0, true))
        gameBoard.addShip(Ship.BATTLESHIP, ShipInfo(4, 3, false))

        gameBoard.fireAt(0, 1)
        gameBoard.fireAt(2, 2)

        val worldBoard = WorldBoard(gameBoard, sender.location)

        return true
    }

}
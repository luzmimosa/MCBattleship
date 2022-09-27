package com.fadedbytes.MCBattleship.game

import com.fadedbytes.MCBattleship.game.player.BattleshipPlayer
import com.fadedbytes.MCBattleship.game.player.ComputerPlayer
import com.fadedbytes.MCBattleship.game.player.HumanPlayer
import org.bukkit.entity.Player

class BattleshipGame(
    player: Player
) {

    val bluePlayer: BattleshipPlayer
    val redPlayer: BattleshipPlayer

    init {
        bluePlayer = HumanPlayer(this, GameBoard(), player)
        redPlayer = ComputerPlayer(this, GameBoard())
    }

    fun getEnemyPlayer(player: BattleshipPlayer): BattleshipPlayer {
        return if (player == bluePlayer) redPlayer else bluePlayer
    }



}
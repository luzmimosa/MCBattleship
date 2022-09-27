package com.fadedbytes.MCBattleship.game.player

import com.fadedbytes.MCBattleship.game.BattleshipGame
import com.fadedbytes.MCBattleship.game.GameBoard
import com.fadedbytes.MCBattleship.game.Ship
import org.bukkit.entity.Player

class HumanPlayer(
    override val linkedGame: BattleshipGame,
    override val gameBoard: GameBoard,
    val player: Player
) : BattleshipPlayer {


    override var lastShotIsHit: Boolean = false;
    override fun endGame() {
        TODO("Not yet implemented")
    }


}
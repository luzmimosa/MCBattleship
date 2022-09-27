package com.fadedbytes.MCBattleship.game.player

import com.fadedbytes.MCBattleship.game.BattleshipGame
import com.fadedbytes.MCBattleship.game.GameBoard

class ComputerPlayer(
    override val linkedGame: BattleshipGame,
    override val gameBoard: GameBoard
): BattleshipPlayer {

    override var lastShotIsHit: Boolean = false

    override fun endGame() {
        // Computer player does not need to do anything here
    }

}
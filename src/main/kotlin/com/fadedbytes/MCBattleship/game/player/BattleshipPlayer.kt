package com.fadedbytes.MCBattleship.game.player

import com.fadedbytes.MCBattleship.game.BattleshipGame
import com.fadedbytes.MCBattleship.game.GameBoard
import com.fadedbytes.MCBattleship.game.Ship
import com.fadedbytes.MCBattleship.game.ShipInfo

interface BattleshipPlayer {

    val linkedGame: BattleshipGame
    val gameBoard: GameBoard

    var lastShotIsHit: Boolean

    fun placeShip(shipType: Ship, x: Int, y: Int, vertical: Boolean) {
        gameBoard.addShip(shipType, ShipInfo(x, y, vertical))
    }

    fun fire(x: Int, y: Int) {
        this.lastShotIsHit = linkedGame.getEnemyPlayer(this).gameBoard.fireAt(x, y)
    }

    fun endGame()

}
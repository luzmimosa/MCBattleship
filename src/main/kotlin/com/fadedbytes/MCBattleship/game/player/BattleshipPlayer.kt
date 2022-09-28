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

    fun placeAllShips() {
        for (ship: Ship in Ship.values()) {
            var iterations = 0;
            while (true) {
                if (iterations > 30) {
                    break
                }
                val coords = this.getRandomCoordinates()
                try {
                    this.placeShip(ship, coords.first, coords.second, Math.random() < 0.5)
                    break
                } catch (e: IllegalArgumentException) {
                    iterations++;
                    continue
                }
            }
        }
    }

    fun getRandomCoordinates(): Pair<Int, Int> {
        val x = (0 until this.gameBoard.boardSize).random()
        val y = (0 until this.gameBoard.boardSize).random()
        return Pair(x, y)
    }

    fun endGame()

}
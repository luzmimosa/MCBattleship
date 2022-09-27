package com.fadedbytes.MCBattleship.game

import org.bukkit.entity.Player

class HumanPlayer(
    override val linkedGame: BattleshipGame,
    override val gameBoard: GameBoard,
    val player: Player
    ) : BattleshipPlayer {

    override fun placeShip(shipType: Ship, x: Int, y: Int, vertical: Boolean) {
        TODO("Not yet implemented")
    }

    override fun fire(x: Int, y: Int) {
        TODO("Not yet implemented")
    }

    override fun endGame() {
        TODO("Not yet implemented")
    }

}
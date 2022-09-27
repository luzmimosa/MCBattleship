package com.fadedbytes.MCBattleship.game

interface BattleshipPlayer {

    val linkedGame: BattleshipGame
    val gameBoard: GameBoard

    fun placeShip(shipType: Ship, x: Int, y: Int, vertical: Boolean)

    fun fire(x: Int, y: Int)

    fun endGame()

}
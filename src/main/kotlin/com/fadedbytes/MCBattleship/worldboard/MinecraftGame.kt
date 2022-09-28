package com.fadedbytes.MCBattleship.worldboard

import com.fadedbytes.MCBattleship.game.BattleshipGame
import com.fadedbytes.MCBattleship.game.GameMaster
import org.bukkit.Location
import org.bukkit.entity.Player

class MinecraftGame(
    val player: Player,
    private val initialLocation: Location
    ){

    val logicGame = BattleshipGame(player)
    lateinit var radar: WorldBoard

    private var state = GameState.NONE

    init {
        GameMaster.pairPlayerGame(player, this)
    }

    fun start() {
        if (state != GameState.NONE) {
            throw IllegalStateException("Game is already started")
        }

        this.createGame()

    }

    private fun createGame() {
        logicGame.let {
            it.bluePlayer.placeAllShips()
            it.redPlayer.placeAllShips()
        }
        setupRadar(initialLocation)
    }

    private fun setupRadar(radarOrigin: Location) {
        radar = WorldBoard(logicGame.redPlayer.gameBoard, radarOrigin)
    }

    private fun playerTurn() {

    }

    private fun computerTurn() {

    }

    private fun endGame() {

    }

}

enum class GameState {
    NONE,
    PLACING_SHIPS,
    PLAYER_TURN,
    COMPUTER_TURN,
    GAME_OVER
}
package com.fadedbytes.MCBattleship.worldboard

import com.fadedbytes.MCBattleship.game.BattleshipGame
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

class MinecraftGame(forPlayer: Player, at: Location) {

    val logicGame = BattleshipGame(forPlayer)
    val worldBoard: WorldBoard

    private var state = GameState.PLACING_SHIPS

    init {

        worldBoard = WorldBoard(logicGame.bluePlayer.gameBoard, at)

        logicGame.bluePlayer.placeAllShips()

    }

    fun playerTurn() {

    }

    fun computerTurn() {

    }

}

enum class GameState {
    NONE,
    PLACING_SHIPS,
    PLAYER_TURN,
    COMPUTER_TURN,
    GAME_OVER
}
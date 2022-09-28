package com.fadedbytes.MCBattleship.worldboard

import com.fadedbytes.MCBattleship.game.BattleshipGame
import com.fadedbytes.MCBattleship.worldboard.events.GameChangedGameStateEvent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

class MinecraftGame(forPlayer: Player, at: Location) {

    val logicGame = BattleshipGame(forPlayer)
    val worldBoard: WorldBoard

    private var state = GameState.PLACING_SHIPS

    init {
        Bukkit.getPluginManager().callEvent(GameChangedGameStateEvent(this, GameState.NONE, GameState.PLACING_SHIPS))

        worldBoard = WorldBoard(logicGame.bluePlayer.gameBoard, at)

        logicGame.bluePlayer.placeAllShips()

    }

}

enum class GameState {
    NONE,
    PLACING_SHIPS,
    PLAYER_TURN,
    COMPUTER_TURN,
    GAME_OVER
}
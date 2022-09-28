package com.fadedbytes.MCBattleship.game

import com.fadedbytes.MCBattleship.worldboard.MinecraftGame
import com.fadedbytes.MCBattleship.worldboard.WorldBoard
import org.bukkit.entity.Player

class GameMaster(){

    companion object {

        private val playingPlayers: MutableMap<Player, MinecraftGame> = mutableMapOf()
        private val boardMap: MutableMap<GameBoard, WorldBoard> = mutableMapOf()

        fun startGameFor(player: Player): MinecraftGame {
            if (isPlaying(player)) {
                throw IllegalStateException("Player is already playing a game")
            }

            val game = MinecraftGame(player, player.location)
            return game

        }

        fun isPlaying(player: Player): Boolean {
            return playingPlayers.containsKey(player)
        }

        fun getLogicalGame(player: Player): BattleshipGame? {
            return playingPlayers[player]?.logicGame
        }

        fun getWorldBoard(gameBoard: GameBoard): WorldBoard? {
            return boardMap[gameBoard]
        }

        fun pairGameBoard(gameBoard: GameBoard, worldBoard: WorldBoard) {
            boardMap[gameBoard] = worldBoard
        }

        fun pairPlayerGame(player: Player, game: MinecraftGame) {
            playingPlayers[player] = game
        }
    }

}
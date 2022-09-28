package com.fadedbytes.MCBattleship.game

import com.fadedbytes.MCBattleship.worldboard.MinecraftGame
import com.fadedbytes.MCBattleship.worldboard.WorldBoard
import com.fadedbytes.MCBattleship.worldboard.events.LogicGameBoardChanged
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameMaster: Listener {

    companion object {

        private val playingPlayers: MutableMap<Player, MinecraftGame> = mutableMapOf()
        val boardMap: MutableMap<GameBoard, WorldBoard> = mutableMapOf()

        fun startGameFor(player: Player): MinecraftGame {
            if (playingPlayers.containsKey(player)) {
                throw IllegalStateException("Player is already playing a game")
            }

            val game = MinecraftGame(player, player.location)
            playingPlayers[player] = game

            return game

        }

        fun isPlaying(player: Player): Boolean {
            return playingPlayers.containsKey(player)
        }

        fun getLogicalGame(player: Player): BattleshipGame? {
            return playingPlayers[player]?.logicGame
        }

        @EventHandler
        fun onLogicalBoardUpdate(event: LogicGameBoardChanged) {
            boardMap[event.gameBoard]?.updateWorld()
        }
    }

}
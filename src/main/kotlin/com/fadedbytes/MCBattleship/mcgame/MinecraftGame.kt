package com.fadedbytes.MCBattleship.mcgame

import com.fadedbytes.MCBattleship.game.BattleshipGame
import com.fadedbytes.MCBattleship.mcgame.api.listeners.ShipPlacementListener
import com.fadedbytes.MCBattleship.mcgame.features.RadarGameboard
import org.bukkit.Location
import org.bukkit.entity.Player

class MinecraftGame(
    val player: Player,
    val location: Location,
    val size: Int
) {

    val logicGame = BattleshipGame(size)
    val radar = RadarGameboard(size, location)

    lateinit var shipPlacementListener: ShipPlacementListener

    init {
        if (isPlaying(player)) {
            throw IllegalArgumentException("Player is already playing a game")
        }

        playersPlaying[player] = this

        setupRadar()
        startShipPlacement()

    }

    companion object {
        val playersPlaying = mutableMapOf<Player, MinecraftGame>()

        fun getGame(player: Player): MinecraftGame? {
            return playersPlaying[player]
        }

        fun isPlaying(player: Player): Boolean {
            return playersPlaying.containsKey(player)
        }
    }

    private fun setupRadar() {
        radar.syncWithGameboard(logicGame.bluePlayer.gameboard)
    }

    private fun startShipPlacement() {

        // Randomize computer ships
        logicGame.redPlayer.gameboard.randomizeShips()

        shipPlacementListener = ShipPlacementListener(player, radar.gameboardArea, game = this)

    }

    fun updateAll() {
        setupRadar()
    }

}
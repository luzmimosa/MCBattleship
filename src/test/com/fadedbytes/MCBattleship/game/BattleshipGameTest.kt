package com.fadedbytes.MCBattleship.game

import com.fadedbytes.MCBattleship.game.board.ship.Ship
import com.fadedbytes.MCBattleship.game.board.ship.ShipInfo
import org.junit.jupiter.api.Test

internal class BattleshipGameTest {
    @Test
    fun `both gameboards are empty on game start`() {
        val game = BattleshipGame(10)

    }

    @Test
    fun `players are full health only after ship placing`() {
        val game = BattleshipGame(10)

        assert(game.bluePlayer.health() == 0)
        assert(game.redPlayer.health() == 0)

        game.bluePlayer.gameboard.placeShip(Ship.CARRIER, ShipInfo(2, 2, true))
        game.redPlayer.gameboard.placeShip(Ship.BATTLESHIP, ShipInfo(3, 3, true))

        assert(game.bluePlayer.health() == Ship.CARRIER.size)
        assert(game.redPlayer.health() == Ship.BATTLESHIP.size)

        assert(game.bluePlayer.maxHealth() == game.bluePlayer.health())
        assert(game.redPlayer.maxHealth() == game.redPlayer.health())
    }
}
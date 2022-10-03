package com.fadedbytes.MCBattleship.mcgame.features.shipboard

import com.fadedbytes.MCBattleship.game.board.ship.Ship
import com.fadedbytes.MCBattleship.util.BlockArea

interface ShipPlacer {

    fun placeShip(ship: Ship, area: BlockArea, tileSize: Int = 3)

}
package com.fadedbytes.MCBattleship.mcgame.features.shipboard

import com.fadedbytes.MCBattleship.game.board.BattleshipGameboard
import com.fadedbytes.MCBattleship.game.board.SimpleGameboard
import com.fadedbytes.MCBattleship.util.BlockArea
import org.bukkit.Location

class Shipboard(
    val gameboard: BattleshipGameboard,
    val originLocation: Location,
    val tileSize: Int = 3
) {

    val shipPlacer = CrystalShipPlacer()

    fun placeShips() {
        for (ship in (gameboard as SimpleGameboard).ships) {
            val shipInfo = ship.value
            val shipType = ship.key
            val shipSize = shipType.size

            val x = shipInfo.x
            val y = shipInfo.y
            val vertical = shipInfo.vertical

            val originCell = getAreaOfCell(x, y)
            var limitCell = getAreaOfCell(x + if (vertical) 0 else shipSize - 1, y + if (vertical) shipSize - 1 else 0)

            val shipOrigin = Location(originLocation.world,
                originCell.minOf { it.x }.toDouble(),
                originCell.minOf { it.y }.toDouble(),
                originCell.minOf { it.z }.toDouble(),
            )

            val shipLimit = Location(originLocation.world,
                limitCell.maxOf { it.x }.toDouble(),
                limitCell.maxOf { it.y }.toDouble(),
                limitCell.maxOf { it.z }.toDouble(),
            )

            shipPlacer.placeShip(shipType, BlockArea(shipOrigin, shipLimit))
        }
    }

    private fun getAreaOfCell(x: Int, y: Int): BlockArea {
        val x1 = originLocation.x + x * tileSize
        val y1 = originLocation.y
        val z1 = originLocation.z + y * tileSize

        val x2 = x1 + tileSize
        val y2 = y1 + tileSize
        val z2 = z1 + tileSize

        return BlockArea(Location(originLocation.world, x1, y1, z1), Location(originLocation.world, x2, y2, z2))
    }



    fun update() {

    }

}
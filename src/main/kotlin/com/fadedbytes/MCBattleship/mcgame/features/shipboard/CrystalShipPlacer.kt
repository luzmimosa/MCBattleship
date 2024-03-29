package com.fadedbytes.MCBattleship.mcgame.features.shipboard

import com.fadedbytes.MCBattleship.game.board.ship.Ship
import com.fadedbytes.MCBattleship.util.BlockArea
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace

class CrystalShipPlacer: ShipPlacer {

    override fun placeShip(ship: Ship, area: BlockArea, tileSize: Int) {

        val mainMaterial = when (ship.name) {
            "CARRIER"       -> Material.BLUE_STAINED_GLASS
            "BATTLESHIP"    -> Material.ORANGE_STAINED_GLASS
            "DESTROYER"     -> Material.GREEN_STAINED_GLASS
            "SUBMARINE"     -> Material.BROWN_STAINED_GLASS
            "CRUISER"       -> Material.BLACK_STAINED_GLASS
            else            -> Material.WHITE_STAINED_GLASS
        }

        val secondaryMaterial = when (ship.name) {
            "CARRIER"       -> Material.LIGHT_BLUE_STAINED_GLASS
            "BATTLESHIP"    -> Material.YELLOW_STAINED_GLASS
            "DESTROYER"     -> Material.LIME_STAINED_GLASS
            "SUBMARINE"     -> Material.RED_STAINED_GLASS
            "CRUISER"       -> Material.WHITE_STAINED_GLASS
            else            -> Material.BLACK_STAINED_GLASS
        }

        val xLength = area.getBlocks().maxOf { it.x } - area.getBlocks().minOf { it.x }
        val yLength = area.getBlocks().maxOf { it.y } - area.getBlocks().minOf { it.y }
        val zLength = area.getBlocks().maxOf { it.z } - area.getBlocks().minOf { it.z }

        val xCenter = area.getBlocks().minOf { it.x } + xLength / 2
        val yCenter = area.getBlocks().minOf { it.y } + yLength / 2
        val zCenter = area.getBlocks().minOf { it.z } + zLength / 2

        val centerLocation = Location(area.getBlocks().first().location.world, xCenter.toDouble(), yCenter.toDouble(), zCenter.toDouble())
        val maxLength = maxOf(xLength, yLength, zLength)

        for (block in area) {
            val distanceToCenter = block.location.distance(centerLocation) + 0.1
            val distanceFactor = distanceToCenter / maxLength

            val seed = Math.random()

            block.type = if (seed >= distanceFactor) mainMaterial else if (seed >= distanceFactor / 4) secondaryMaterial else Material.AIR

        }

        for (block in area) {
            var airFound = false
            for (face in listOf(BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST)) {
                if (block.getRelative(face).type == Material.AIR) {
                    airFound = true
                    break
                }
            }

            if (!airFound) {
                block.type = Material.SEA_LANTERN
            }
        }

    }

}
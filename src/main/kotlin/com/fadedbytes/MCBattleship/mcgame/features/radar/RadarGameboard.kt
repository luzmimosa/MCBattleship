package com.fadedbytes.MCBattleship.mcgame.features.radar

import com.fadedbytes.MCBattleship.game.board.BattleshipGameboard
import com.fadedbytes.MCBattleship.mcgame.features.MinecraftGameboard
import org.bukkit.Location

class RadarGameboard(
    size: Int,
    initialLocation: Location
): MinecraftGameboard(size, initialLocation) {

    var palette: RadarPalette = RadarPalettes.DEFAULT.palette

    override fun syncWithGameboard(gameboard: BattleshipGameboard) {
        for (x in 0 until gameboardSize) {
            for (y in 0 until gameboardSize) {
                val cellState = gameboard.getCellState(x, y)
                val block = gameboardArea.getRelativeBlock(x, 0, y)

                block.type = palette.getMaterial(cellState)

            }
        }
    }


}
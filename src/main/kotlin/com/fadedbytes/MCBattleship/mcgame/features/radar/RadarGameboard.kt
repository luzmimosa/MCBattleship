package com.fadedbytes.MCBattleship.mcgame.features.radar

import com.fadedbytes.MCBattleship.game.board.BattleshipGameboard
import com.fadedbytes.MCBattleship.game.board.CellState
import com.fadedbytes.MCBattleship.mcgame.features.MinecraftGameboard
import org.bukkit.Location

class RadarGameboard(
    size: Int,
    initialLocation: Location,
    var showShips: Boolean = false
): MinecraftGameboard(size, initialLocation) {

    private var lastSync: BattleshipGameboard? = null

    var palette: RadarPalette = RadarPalettes.DEFAULT.palette
        set(value) {
            field = value
            lastSync?.let { syncWithGameboard(it) }
        }

    override fun syncWithGameboard(gameboard: BattleshipGameboard) {
        for (x in 0 until gameboardSize) {
            for (y in 0 until gameboardSize) {
                var cellState = gameboard.getCellState(x, y)

                if (!showShips && cellState == CellState.SHIP) {
                    cellState = CellState.EMPTY
                }

                val block = gameboardArea.getRelativeBlock(x, 0, y)

                block.type = palette.getMaterial(cellState)

            }
        }
    }


}
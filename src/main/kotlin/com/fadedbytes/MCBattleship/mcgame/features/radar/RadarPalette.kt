package com.fadedbytes.MCBattleship.mcgame.features.radar

import com.fadedbytes.MCBattleship.game.board.CellState
import org.bukkit.Material

@FunctionalInterface
interface RadarPalette {
    fun getMaterial(state: CellState): Material
}

enum class RadarPalettes(val palette: RadarPalette) {
    DEFAULT(object : RadarPalette {
        override fun getMaterial(state: CellState): Material {
            return when (state) {
                CellState.EMPTY -> Material.BROWN_CONCRETE_POWDER
                CellState.SHIP -> Material.BLACK_CONCRETE
                CellState.HIT -> Material.TARGET
                CellState.MISS -> Material.GRAY_TERRACOTTA
            }
        }
    }),
    ENEMY(object : RadarPalette {
        override fun getMaterial(state: CellState): Material {
            return when (state) {
                CellState.EMPTY -> Material.WHITE_STAINED_GLASS
                CellState.SHIP -> Material.SCULK
                CellState.HIT -> Material.TARGET
                CellState.MISS -> Material.LIGHT_BLUE_STAINED_GLASS
            }
        }
    }),
}
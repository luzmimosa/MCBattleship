package com.fadedbytes.MCBattleship.worldboard

import com.fadedbytes.MCBattleship.game.CellState
import com.fadedbytes.MCBattleship.game.GameBoard
import org.bukkit.Location
import org.bukkit.Material

class WorldBoard(
    val gameBoard: GameBoard,
    val initialLocation: Location
) {

    private val board: Array<Array<Material>> = Array(gameBoard.boardSize) { Array(gameBoard.boardSize) { Material.BLUE_TERRACOTTA } }

    init {
        synchronizeBoards()
        updateWorld()
    }

    fun synchronizeBoards() {
        for (x in 0 until gameBoard.boardSize) {
            for (y in 0 until gameBoard.boardSize) {
                board[x][y] = when (gameBoard.getCellState(x, y)) {
                    CellState.EMPTY -> Material.BLUE_TERRACOTTA
                    CellState.SHIP -> Material.BLACK_TERRACOTTA
                    CellState.HIT -> Material.RED_TERRACOTTA
                    CellState.MISS -> Material.YELLOW_TERRACOTTA
                }
            }
        }
    }

    fun updateWorld() {
        val initialX = initialLocation.blockX
        val initialY = initialLocation.blockY
        val initialZ = initialLocation.blockZ

        for (i in 0 until gameBoard.boardSize) {
            for (j in 0 until gameBoard.boardSize) {
                val block = initialLocation.world?.getBlockAt(initialX + i, initialY, initialZ + j)
                block?.setType(board[i][j])
            }
        }
    }

}
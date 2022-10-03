package com.fadedbytes.MCBattleship.game.player

import com.fadedbytes.MCBattleship.game.board.BattleshipGameboard
import com.fadedbytes.MCBattleship.game.board.CellState

/**
 * A battleship player has a gameboard and a health bar. The current health is the
 * current number of ship cells at the gameboard. The max health is the current
 * health plus the number of ship cells that have been hit.
 */
interface BattleshipPlayer {

    /**
     * The gameboard linked to this player
     */
    val gameboard: BattleshipGameboard

    /**
     * The max health of this player
     */
    fun maxHealth(): Int {
        var health = 0
        for (x in 0 until gameboard.size) {
            for (y in 0 until gameboard.size) {
                when (gameboard.getCellState(x, y)) {
                    CellState.SHIP, CellState.HIT -> health++
                    else -> {}
                }
            }
        }
        return health
    }

    /**
     * The current health of this player
     */
    fun health(): Int {
        var health = 0
        for (x in 0 until gameboard.size) {
            for (y in 0 until gameboard.size) {
                when (gameboard.getCellState(x, y)) {
                    CellState.SHIP -> health++
                    else -> {}
                }
            }
        }
        return health
    }



}
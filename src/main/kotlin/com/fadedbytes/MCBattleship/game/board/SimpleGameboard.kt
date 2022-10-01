package com.fadedbytes.MCBattleship.game.board

import com.fadedbytes.MCBattleship.game.board.ship.Ship
import com.fadedbytes.MCBattleship.game.board.ship.ShipInfo

class SimpleGameboard(
    override val size: Int
    ) : BattleshipGameboard {

    override var frozen: Boolean = false

    val cells: Array<Array<CellState>> = Array(size) { Array(size) { CellState.EMPTY } }
    val ships: MutableMap<Ship, ShipInfo> = mutableMapOf()


    override fun getCellState(x: Int, y: Int): CellState {
        return cells[x][y]
    }

    override fun setCellState(x: Int, y: Int, state: CellState) {
        if (frozen) {
            throw IllegalStateException("Gameboard is frozen")
        }
        cells[x][y] = state
    }

    override fun allShipsPlaced(): Boolean {
        return ships.keys.size == Ship.values().size
    }

    override fun registerFire(x: Int, y: Int): Boolean {
        val currentState = getCellState(x, y)

        when (currentState) {
            CellState.EMPTY -> {
                setCellState(x, y, CellState.MISS)
                return false
            }
            CellState.SHIP -> {
                setCellState(x, y, CellState.HIT)
                return true
            }
            else -> {
                throw IllegalArgumentException("Cell already hit")
            }
        }

    }

    override fun randomizeShips() {
        for (ship in Ship.values()) {
            var iterations = 0
            do {
                try {
                    placeShip(
                        ship,
                        ShipInfo(
                            (0 until size).random(),
                            (0 until size).random(),
                            (0..1).random() == 1)
                    )
                    break
                } catch (e: IllegalArgumentException) {
                    continue
                }
            } while (iterations++ < 255)
        }
    }

    override fun placeShip(ship: Ship, info: ShipInfo) {
        if (!canPlaceShip(ship, info)) {
            throw IllegalArgumentException("Cannot place ship")
        }

        ships[ship] = info

        for (i in 0 until ship.size) {
            val x = if (info.vertical) info.x       else info.x + i
            val y = if (info.vertical) info.y + i   else info.y
            setCellState(x, y, CellState.SHIP)
        }

    }

    override fun canPlaceShip(ship: Ship, info: ShipInfo): Boolean {

        // Gameboard is frozen
        if (frozen) {
            return false
        }

        // Ship is already placed
        if (ships.keys.contains(ship)) {
            return false
        }

        // Ship origin is out of bounds
        if (
            info.x < 0 || info.x >= size ||
            info.y < 0 || info.y >= size
        ) {
            return false
        }

        // Ship is out of bounds
        if (info.vertical) {
            if (info.y + ship.size > size) {
                return false
            }
        } else {
            if (info.x + ship.size > size) {
                return false
            }
        }

        // Ship can be placed

        return true
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (y in 0 until size) {
            for (x in 0 until size) {
                sb.append(getCellState(x, y).symbol)
            }
            sb.append("\n")
        }
        return sb.toString()

    }
}
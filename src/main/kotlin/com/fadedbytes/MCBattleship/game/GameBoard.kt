package com.fadedbytes.MCBattleship.game

class GameBoard(val boardSize: Int = 10) {

    private val ships = mutableMapOf<Ship, ShipInfo>()
    private val board = Array(boardSize) { Array(boardSize) { CellState.EMPTY } }


    fun containsShip(ship: Ship): Boolean {
        return ships.containsKey(ship)
    }

    fun addShip(ship: Ship, shipInfo: ShipInfo) {
        if (containsShip(ship)) {
            throw IllegalArgumentException("Ship already exists")
        }

        if (shipCanBePlacedAt(ship, shipInfo)) {
            ships.put(ship, shipInfo)
        } else {
            throw IllegalArgumentException("Ship cannot be placed at the specified location")
        }
    }

    fun shipCanBePlacedAt(ship: Ship, shipInfo: ShipInfo): Boolean {
        if (
            shipInfo.x < 0 || shipInfo.x >= boardSize ||
            shipInfo.y < 0 || shipInfo.y >= boardSize ||
            (!shipInfo.vertical && shipInfo.x + ship.size > boardSize) ||
            ( shipInfo.vertical && shipInfo.y + ship.size > boardSize)
        ) {
            return false
        }

        // check if ship overlaps with another ship
        for (i in 0 until ship.size) {
            val x = if (shipInfo.vertical) shipInfo.x else shipInfo.x + i
            val y = if (shipInfo.vertical) shipInfo.y + i else shipInfo.x
            if (board[x][y] != CellState.EMPTY) {
                return false
            }
        }

        return true
    }

    fun fireAt(x: Int, y: Int): Boolean {
        val state: CellState = getCellState(x, y)
        if (state in (CellState.HIT..CellState.MISS)) {
            throw IllegalArgumentException("Cell already hit")
        }

        getShipAt(x, y)?.let {
            setCellState(x, y, CellState.HIT)
            return true
        }

        setCellState(x, y, CellState.MISS)
        return false
    }

    fun getCellState(x: Int, y: Int): CellState {
        getShipAt(x, y)?.let {
            return CellState.SHIP
        }
        return board[x][y]
    }

    fun setCellState(x: Int, y: Int, state: CellState) {
        board[x][y] = state
    }

    fun getShipAt(x: Int, y: Int): Ship? {
        for (ship: Ship in ships.keys) {
            val shipInfo: ShipInfo = ships[ship] ?: return null

            if (shipInfo.vertical) {
                if (shipInfo.x == x && shipInfo.y <= y && shipInfo.y + ship.size > y) {
                    return ship
                }
            } else {
                if (shipInfo.y == y && shipInfo.x <= x && shipInfo.x + ship.size > x) {
                    return ship
                }
            }
        }
        return null
    }

    fun getMaxHealth(): Int {
        return this.ships.keys.sumOf { it.size }
    }

    fun getCurrentHealth(): Int {
        var health = getMaxHealth()
        for (x in 0 until boardSize) {
            for (y in 0 until boardSize) {
                if (getCellState(x, y) == CellState.HIT) {
                    health--
                }
            }
        }
        return health
    }

}

enum class CellState {
    EMPTY, SHIP, HIT, MISS
}
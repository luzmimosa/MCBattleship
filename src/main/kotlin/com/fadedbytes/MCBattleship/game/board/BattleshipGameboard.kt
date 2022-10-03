package com.fadedbytes.MCBattleship.game.board

import com.fadedbytes.MCBattleship.game.board.ship.Ship
import com.fadedbytes.MCBattleship.game.board.ship.ShipInfo


interface BattleshipGameboard {

    /**
     * The size of the gameboard. It is assumed that the gameboard is square.
     */
    val size: Int

    /**
     * When the gameboard is frozen, no more ships can be added to it.
     */
    var frozen: Boolean


    /**
     * Gets the state of the specified cell at this gameboard.
     * @param x The x coordinate of the cell.
     * @param y The y coordinate of the cell.
     * @return The state of the cell.
     */
    fun getCellState(x: Int, y: Int): CellState

    /**
     * Sets the state of the specified cell at this gameboard. If the cell is already in the specified state, nothing happens.
     */
    fun setCellState(x: Int, y: Int, state: CellState)

    /**
     * @return whether all ships have been placed on this gameboard.
     */
    fun allShipsPlaced(): Boolean

    /**
     * Registers a fire at the specified cell.
     * @return whether the fire hit a ship.
     */
    fun registerFire(x: Int, y: Int): Boolean

    /**
     * Places all ships randomly on this gameboard. It will wipe any existing ships.
     * This method will not work if the gameboard is already frozen.
     * @throws IllegalStateException if the gameboard is frozen.
     */
    fun randomizeShips()

    /**
     * Adds a ship to this gameboard. This method will not work if the gameboard is already frozen.
     * @param ship The ship type to add.
     * @param info The information of the ship to add.
     * @throws IllegalArgumentException if there is already a ship of the same type at the board, or if the ship cannot be placed at the specified location.
     * @throws IllegalStateException if the gameboard is frozen
     */
    fun placeShip(ship: Ship, info: ShipInfo)

    /**
     * @return whether the specified ship can be placed at the specified location.
     */
    fun canPlaceShip(ship: Ship, info: ShipInfo): Boolean

}
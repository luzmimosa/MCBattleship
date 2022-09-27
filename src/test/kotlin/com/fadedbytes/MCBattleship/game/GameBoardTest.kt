package com.fadedbytes.MCBattleship.game

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GameBoardTest {

    @Test
    fun `correct game board size`() {
        val gameBoard = GameBoard(10)
        assertEquals(10, gameBoard.boardSize)
    }

    @Test
    fun `correct game board size with default`() {
        val gameBoard = GameBoard()
        assertEquals(10, gameBoard.boardSize)
    }

    @Test()
    fun `can't place boats out of bounds`() {
        val gameBoard = GameBoard(10)
        assertThrows(IllegalArgumentException::class.java) {
            gameBoard.addShip(Ship.BATTLESHIP, ShipInfo(-1, 0 , true))
        }
        assertThrows(IllegalArgumentException::class.java) {
            gameBoard.addShip(Ship.BATTLESHIP, ShipInfo(0, -1 , true))
        }

        assertThrows(IllegalArgumentException::class.java) {
            gameBoard.addShip(Ship.BATTLESHIP, ShipInfo(10, 0 , true))
        }

        assertThrows(IllegalArgumentException::class.java) {
            gameBoard.addShip(Ship.CARRIER, ShipInfo(0, 6 , true))
        }
        assertThrows(IllegalArgumentException::class.java) {
                gameBoard.addShip(Ship.CARRIER, ShipInfo(6, 0 , false))
        }
    }

    @Test
    fun `can't place boats on top of each other`() {
        val gameBoard = GameBoard(10)
        gameBoard.addShip(Ship.BATTLESHIP, ShipInfo(0, 0 , true))
        assertThrows(IllegalArgumentException::class.java) {
            gameBoard.addShip(Ship.BATTLESHIP, ShipInfo(0, 0 , true))
        }
    }

    @Test
    fun `correct max health`() {
        val gameBoard = GameBoard(10)

        gameBoard.addShip(Ship.BATTLESHIP, ShipInfo(0, 0 , true))
        assertEquals(4, gameBoard.getMaxHealth())

        gameBoard.addShip(Ship.CARRIER, ShipInfo(0, 1 , true))
        assertEquals(9, gameBoard.getMaxHealth())
    }

}

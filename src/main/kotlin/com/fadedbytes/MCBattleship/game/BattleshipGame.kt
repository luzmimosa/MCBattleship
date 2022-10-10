package com.fadedbytes.MCBattleship.game

import com.fadedbytes.MCBattleship.game.player.BattleshipPlayer
import com.fadedbytes.MCBattleship.game.player.SimplePlayer

class BattleshipGame(
    boardSize: Int
    ) {

    val bluePlayer: BattleshipPlayer
    val redPlayer: BattleshipPlayer

    var gameState: GameState = GameState.PLACING_SHIPS
        private set

    init {
        bluePlayer = SimplePlayer(boardSize)
        redPlayer = SimplePlayer(boardSize)
    }

    private fun getOpponent(player: BattleshipPlayer): BattleshipPlayer {
        return if (isBluePlayer(player)) {
            redPlayer
        } else if (isRedPlayer(player)) {
            bluePlayer
        } else {
            throw IllegalArgumentException("Player is not part of this game")
        }
    }

    private fun isRedPlayer(player: BattleshipPlayer): Boolean {
        return player == redPlayer
    }

    private fun isBluePlayer(player: BattleshipPlayer): Boolean {
        return player == bluePlayer
    }

    /**
     * Registers a fire from the given player at the given coordinates.
     */
    fun fire(player: BattleshipPlayer, x: Int, y: Int): Boolean {
        when (gameState) {
            GameState.PLACING_SHIPS -> {
                throw IllegalStateException("Cannot fire while ships are being placed")
            }
            GameState.BLUE_TURN -> {
                if (!isBluePlayer(player)) {
                    throw IllegalArgumentException("It is not this player's turn")
                }
            }
            GameState.RED_TURN -> {
                if (!isRedPlayer(player)) {
                    throw IllegalArgumentException("It is not this player's turn")
                }
            }
            GameState.BLUE_WON, GameState.RED_WON -> {
                throw IllegalStateException("Game is already over")
            }
        }

        return getOpponent(player).gameboard.registerFire(x, y)
    }

    /**
     * Advances the game state to the next turn, or to the end of the game if a player has 0 health points.
     */
    fun nextGameState() {
        when (gameState) {
            GameState.PLACING_SHIPS -> {
                if (bluePlayer.gameboard.allShipsPlaced() && redPlayer.gameboard.allShipsPlaced()) {
                    gameState = if ((0..1).random() == 1) GameState.BLUE_TURN else GameState.RED_TURN
                } else {
                    throw IllegalStateException("Not all ships have been placed")
                }
            }
            GameState.BLUE_TURN -> {
                gameState = if (redPlayer.health() == 0) {
                    GameState.BLUE_WON
                } else {
                    GameState.RED_TURN
                }
            }
            GameState.RED_TURN -> {
                gameState = if (bluePlayer.health() == 0) {
                    GameState.RED_WON
                } else {
                    GameState.BLUE_TURN
                }
            }
            GameState.BLUE_WON -> {
                throw IllegalStateException("Game is already over")
            }
            GameState.RED_WON -> {
                throw IllegalStateException("Game is already over")
            }
        }
    }

}
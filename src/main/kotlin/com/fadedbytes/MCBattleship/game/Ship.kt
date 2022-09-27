package com.fadedbytes.MCBattleship.game

enum class Ship(val size: Int) {
    CARRIER(5),
    BATTLESHIP(4),
    CRUISER(3),
    SUBMARINE(3),
    DESTROYER(2);
}

data class ShipInfo(
    val x: Int,
    val y: Int,
    val vertical: Boolean
)
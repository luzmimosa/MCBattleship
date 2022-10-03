package com.fadedbytes.MCBattleship.game.board

enum class CellState(val symbol: String) {
    EMPTY("□ "),
    SHIP("■ "),
    HIT("▣ "),
    MISS("▩ ")

}
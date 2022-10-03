package com.fadedbytes.MCBattleship.game.player

import com.fadedbytes.MCBattleship.game.board.BattleshipGameboard
import com.fadedbytes.MCBattleship.game.board.SimpleGameboard

class SimplePlayer(boardSize: Int) : BattleshipPlayer {

    override val gameboard: BattleshipGameboard = SimpleGameboard(boardSize)

}
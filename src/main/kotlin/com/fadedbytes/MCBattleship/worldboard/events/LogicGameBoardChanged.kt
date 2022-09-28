package com.fadedbytes.MCBattleship.worldboard.events

import com.fadedbytes.MCBattleship.game.CellState
import com.fadedbytes.MCBattleship.game.GameBoard
import com.fadedbytes.MCBattleship.worldboard.MinecraftGame
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class LogicGameBoardChanged(
    override val gameBoard: GameBoard,
    val position: Pair<Int, Int>,
    val from: CellState,
    val to: CellState
    ) : Event(), BattleshipEvent, LogicGameboardEvent {

    override fun getHandlers(): HandlerList {
        return HANDLERS_LIST
    }

    companion object {

        private val HANDLERS_LIST = HandlerList()
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HandlerList()
        }
    }
}
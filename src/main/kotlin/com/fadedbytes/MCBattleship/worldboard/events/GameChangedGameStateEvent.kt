package com.fadedbytes.MCBattleship.worldboard.events

import com.fadedbytes.MCBattleship.worldboard.GameState
import com.fadedbytes.MCBattleship.worldboard.MinecraftGame
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class GameChangedGameStateEvent(
    val game: MinecraftGame,
    val oldState: GameState,
    val newState: GameState
) : Event(), BattleshipEvent {

    private val HANDLERS_LIST = HandlerList()

    override fun getHandlers(): HandlerList {
        return HANDLERS_LIST
    }
}
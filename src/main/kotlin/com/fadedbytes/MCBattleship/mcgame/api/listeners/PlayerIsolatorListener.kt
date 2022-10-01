package com.fadedbytes.MCBattleship.mcgame.api.listeners

import com.fadedbytes.MCBattleship.mcgame.MinecraftGame
import org.bukkit.event.*
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.RegisteredListener
import org.bukkit.plugin.java.JavaPlugin


/**
 * This class is used to isolate players from the rest of the world while they are playing a game
 */
class PlayerIsolatorListener(plugin: JavaPlugin): Listener {

    init {
        val registeredListener = RegisteredListener(this,
            { _: Listener?, event: Event -> onPlayerDoLiterallyAnything(event) }, EventPriority.NORMAL, plugin, false
        )
        for (handler in HandlerList.getHandlerLists()) handler.register(registeredListener)


    }

    fun onPlayerDoLiterallyAnything(event: Event) {

        if (event !is PlayerEvent) return

        // Skip if the player is not playing
        if (!MinecraftGame.isPlaying(event.player)) {
            return
        }

        // Skip if it is a permitted event
        if (getPermittedEvents().contains(event.javaClass)) {
            return
        }

        // Cancel the event
        if (event is Cancellable) {
            event.isCancelled = true
//            event.player.spigot().sendMessage(
//                ChatMessageType.ACTION_BAR,
//                TextComponent("[${LocalTime.now().format(DateTimeFormatter.ISO_TIME)}] Cancelled event: ${event.javaClass.simpleName}")
//            )
        }
    }

    fun getPermittedEvents(): List<Class<out PlayerEvent>> {
        return listOf(
            PlayerMoveEvent::class.java,
            PlayerItemHeldEvent::class.java,
            PlayerCommandPreprocessEvent::class.java
        )
    }

}
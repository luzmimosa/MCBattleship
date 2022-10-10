package com.fadedbytes.MCBattleship

import com.fadedbytes.MCBattleship.command.FastTestCommand
import com.fadedbytes.MCBattleship.mcgame.MinecraftGame
import com.fadedbytes.MCBattleship.command.SetupCommand
import com.fadedbytes.MCBattleship.command.StartGameCommand
import com.fadedbytes.MCBattleship.mcgame.api.listeners.PlayerIsolatorListener
import org.bukkit.plugin.java.JavaPlugin


class BattleshipPlugin: JavaPlugin() {

    companion object {
        lateinit var instance: BattleshipPlugin
        val activeGames: MutableSet<MinecraftGame> = mutableSetOf()
    }

    override fun onEnable() {
        instance = this
        logger.info("BattleshipPlugin enabled")

        super.getCommand("test")?.setExecutor(FastTestCommand())
        super.getCommand("start")?.setExecutor(StartGameCommand())
        super.getCommand("setup")?.setExecutor(SetupCommand())

        PlayerIsolatorListener(this)
    }

    override fun onDisable() {
        for (game in activeGames) {
            game.endGame()
        }
        logger.info("BattleshipPlugin disabled")
    }
}
package com.fadedbytes.MCBattleship

import com.fadedbytes.MCBattleship.command.FastTestCommand
import com.fadedbytes.MCBattleship.game.GameMaster
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class BattleshipPlugin: JavaPlugin() {
    override fun onEnable() {
        logger.info("BattleshipPlugin enabled")

        super.getCommand("test")?.setExecutor(FastTestCommand())
        Bukkit.getPluginManager().registerEvents(GameMaster(), this)
    }

    override fun onDisable() {
        logger.info("BattleshipPlugin disabled")
    }
}
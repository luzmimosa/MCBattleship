package com.fadedbytes.MCBattleship

import com.fadedbytes.MCBattleship.command.FastTestCommand
import org.bukkit.plugin.java.JavaPlugin

class BattleshipPlugin: JavaPlugin() {
    override fun onEnable() {
        logger.info("BattleshipPlugin enabled")

        super.getCommand("test")?.setExecutor(FastTestCommand())
    }

    override fun onDisable() {
        logger.info("BattleshipPlugin disabled")
    }
}
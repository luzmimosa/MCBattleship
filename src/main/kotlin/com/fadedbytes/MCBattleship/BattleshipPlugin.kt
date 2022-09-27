package com.fadedbytes.MCBattleship

import org.bukkit.plugin.java.JavaPlugin

class BattleshipPlugin: JavaPlugin() {
    override fun onEnable() {
        logger.info("BattleshipPlugin enabled")
    }

    override fun onDisable() {
        logger.info("BattleshipPlugin disabled")
    }
}
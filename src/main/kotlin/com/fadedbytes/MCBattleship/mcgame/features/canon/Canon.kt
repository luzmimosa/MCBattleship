package com.fadedbytes.MCBattleship.mcgame.features.canon

import org.bukkit.Location

interface Canon {

    fun prepareShoot(fromCoords: Location? = null, afterShoot: () -> Unit)

    fun shoot()

    fun postShoot(afterShoot: () -> Unit)

}
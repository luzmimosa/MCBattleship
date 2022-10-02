package com.fadedbytes.MCBattleship.mcgame.features.canon

import org.bukkit.Location

interface Canon {

    fun prepareShoot(fromCoords: Location? = null)

    fun shoot()

    fun postShoot()

}
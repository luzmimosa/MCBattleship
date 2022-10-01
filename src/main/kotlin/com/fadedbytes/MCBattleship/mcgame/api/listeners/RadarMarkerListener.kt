package com.fadedbytes.MCBattleship.mcgame.api.listeners

import com.fadedbytes.MCBattleship.BattleshipPlugin
import com.fadedbytes.MCBattleship.mcgame.features.HolographicPlaceholder
import com.fadedbytes.MCBattleship.util.BlockArea
import org.bukkit.Axis
import org.bukkit.Bukkit
import org.bukkit.FluidCollisionMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.EquipmentSlot

class RadarMarkerListener(
    val player: Player,
    val radarArea: BlockArea,
    val size: Int,
        material: Material,
    val onHoverOverArea: (x: Int, y: Int, axis: Axis) -> Boolean,
    val onLeftClick: (x: Int, y: Int, axis: Axis) -> Unit
): Listener {

    var hologram: HolographicPlaceholder? = null
    var currentAxis: Axis = Axis.X
        set(value) {
            field = value
            hologram?.axis = value
        }
    var currentBlock: Block? = null
    var material: Material = material
        set(value) {
            field = value
            hologram?.material = value
        }

    init {
        Bukkit.getPluginManager().registerEvents(this, BattleshipPlugin.instance)
    }

    @EventHandler
    fun onPlayerLook(event: PlayerMoveEvent) {

        // Return if the player is not the one we are tracking
        if (event.player != player) return
        val player = event.player

        // Raytrace to find the block the player is looking at
        val raytrace = player.world.rayTraceBlocks(player.eyeLocation, player.location.direction, 20.0, FluidCollisionMode.NEVER, true)

        // If the raytrace is null or it did not hit a block, hide the hologram and return
        if (raytrace == null) {
            hideHologram()
            return
        }

        val block = raytrace.hitBlock
        if (block == null) {
            hideHologram()
            return
        }


        // Check if the block is inside the radar area
        if (!radarArea.isInArea(block)) {
            hideHologram()
            return
        }


        // Get the x and y coordinates of the block
        val relativeCoords = radarArea.getRelativeCoordinatesOf(block)
        val x = relativeCoords.first
        val y = relativeCoords.third

        // Call the onHoverOverArea function, and move the hologram anyway
        currentBlock = block
        showHologramAt(block, raytrace.hitBlockFace ?: BlockFace.UP)
        onHoverOverArea(x, y, currentAxis)
    }

    private fun showHologramAt(block: Block, face: BlockFace) {
        val loc = block.getRelative(face).location
        if (hologram == null) {
            hologram = HolographicPlaceholder(material, size, true, loc, currentAxis)
        } else {
            hologram?.move(loc)
        }
    }

    private fun hideHologram() {
        hologram?.remove()
        hologram = null
    }

    @EventHandler
    fun onPlayerClick(event: PlayerInteractEvent) {

        // If the player is not the one we are tracking, return
        if (event.player != player) return

        // If there is not any selected block, or it is not inside the radar area, return
        val block = currentBlock ?: return
        if (!radarArea.isInArea(block)) return

        // Get the x and y coordinates of the block
        val relativeCoords = radarArea.getRelativeCoordinatesOf(block)

        when (event.action) {

            // Left click activates the onLeftClick function
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                onLeftClick(relativeCoords.first, relativeCoords.third, currentAxis)
            }

            // Right click changes the axis
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                if (event.hand == EquipmentSlot.OFF_HAND) return
                currentAxis = if (currentAxis == Axis.X) Axis.Z else Axis.X
                showHologramAt(block, event.blockFace)
            }

            else -> {}
        }

    }

    fun removeHologram() {
        hologram?.remove()
        hologram = null
    }

}
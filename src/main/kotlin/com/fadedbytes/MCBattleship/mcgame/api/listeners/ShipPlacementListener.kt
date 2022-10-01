package com.fadedbytes.MCBattleship.mcgame.api.listeners

import com.fadedbytes.MCBattleship.BattleshipPlugin
import com.fadedbytes.MCBattleship.game.board.ship.Ship
import com.fadedbytes.MCBattleship.game.board.ship.ShipInfo
import com.fadedbytes.MCBattleship.mcgame.MinecraftGame
import com.fadedbytes.MCBattleship.util.BlockArea
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class ShipPlacementListener(
    val player: Player,
    val radarArea: BlockArea,
    val ships: Array<Ship> = Ship.values(),
    val game: MinecraftGame
): Listener {

    private val logicGameboard = game.logicGame.bluePlayer.gameboard

    private var shipPlaceholderTrain: PlaceholderTrain? = null
    var selectedBlock: Block? = null
    var currentShip: Ship? = null
        set(value) {
            field = value
            shipPlaceholderTrain?.remove()
            if (value != null) {
                selectedBlock?.let {
                    shipPlaceholderTrain = PlaceholderTrain(value.size, it.location, currentShipAxis)
                }
            }

        }
    var currentShipAxis = Axis.X
        set(value) {
            field = value
            shipPlaceholderTrain?.axis = value
        }

    init {
        Bukkit.getPluginManager().registerEvents(this, BattleshipPlugin.instance)
        currentShip = nextShip()
    }

    @EventHandler
    fun onPlayerLook(event: PlayerMoveEvent) {
        if (event.player != player) return
        val player = event.player

        val raytrace = player.world.rayTraceBlocks(player.eyeLocation, player.location.direction, 20.0, FluidCollisionMode.NEVER, true)
        if (raytrace == null) {
            hideMarker()
            return
        }

        val block = raytrace.hitBlock
        if (block == null) {
            hideMarker()
            return
        }

        if (radarArea.isInArea(block)) {
            selectedBlock = block

            val relativeCoords = radarArea.getRelativeCoordinatesOf(block)
            val x = relativeCoords.first
            val z = relativeCoords.third

            val ship = currentShip ?: return

            if (!logicGameboard.canPlaceShip(ship, ShipInfo(x, z, currentShipAxis == Axis.Z))) {
                return
            }

            moveMarker(block.location.add(
                when (raytrace.hitBlockFace) {
                    BlockFace.NORTH -> Vector(0.5, 0.5, 0.0)
                    BlockFace.EAST -> Vector(1.0, 0.5, 0.5)
                    BlockFace.SOUTH -> Vector(0.5, 0.5, 1.0)
                    BlockFace.WEST -> Vector(0.0, 0.5, 0.5)
                    BlockFace.UP -> Vector(0.5, 1.0, 0.5)
                    BlockFace.DOWN -> Vector(0.5, 0.0, 0.5)
                    else -> Vector(0.5, 0.5, 0.5)
                }
            ))
        } else {
            hideMarker()
        }

    }

    fun createMarker(ship: Ship) {
        val block = selectedBlock ?: return
        shipPlaceholderTrain = PlaceholderTrain(ship.size, block.location, currentShipAxis)
    }

    fun hideMarker() {

        selectedBlock = null

        shipPlaceholderTrain?.remove()
        shipPlaceholderTrain = null
    }

    fun moveMarker(at: Location) {
        if (shipPlaceholderTrain == null) {
            createMarker(currentShip ?: return)
        }
        shipPlaceholderTrain?.move(at)
    }

    fun end(){
        shipPlaceholderTrain?.remove()
        HandlerList.unregisterAll(this)
    }

    @EventHandler
    fun onPlayerClick(event: PlayerInteractEvent) {
        if (event.player != player) return

        val block = selectedBlock ?: return
        if (!radarArea.isInArea(block)) return

        val relativeCoords = radarArea.getRelativeCoordinatesOf(block)

        when (event.action) {
            Action.LEFT_CLICK_BLOCK, Action.LEFT_CLICK_AIR -> {
                val shipInfo = ShipInfo(relativeCoords.first, relativeCoords.third, currentShipAxis == Axis.Z)
                if (logicGameboard.canPlaceShip(currentShip ?: return, shipInfo)) {
                    logicGameboard.placeShip(currentShip ?: return, shipInfo)
                    game.updateAll()
                    currentShip = nextShip()

                    hideMarker()
                }
            }
            Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR -> {
                if (event.hand == EquipmentSlot.OFF_HAND) return
                currentShipAxis = when (currentShipAxis) {
                    Axis.X -> Axis.Z
                    Axis.Z -> Axis.X
                    else -> Axis.X
                }
            }
            else -> return
        }

    }

    private fun nextShip(): Ship? {

        val index = ships.indexOf(currentShip)
        return if (index == -1) {
            ships[0]
        } else {
            if (index == ships.size - 1) {
                null
            } else {
                ships[index + 1]
            }
        }
    }

}

private class PlaceholderTrain(
    size: Int,
    location: Location,
    axis: Axis
    ) {

    val entities: MutableList<ArmorStand>
    var axis = axis
    set(value) {
        field = value
        move(entities[0].location, false)
    }

    init {

        val offsetLocation = applyOffset(location)

        entities = mutableListOf()
        for (i in (0 until size + size - 1)) {

            val loc = offsetLocation.clone().add(
                when (axis) {
                    Axis.X -> Vector(i.toDouble() * 0.5, 0.0, 0.0)
                    Axis.Y -> Vector(0.0, i.toDouble() * 0.5, 0.0)
                    Axis.Z -> Vector(0.0, 0.0, i.toDouble() * 0.5)
                }
            )

            val entity = offsetLocation.world?.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand

            entity.isInvisible = true
            entity.isInvulnerable = true
            entity.isGlowing = true
            entity.setGravity(false)
            entity.setAI(false)
            entity.setMarker(true)

            entity.equipment?.helmet = ItemStack(Material.BLACK_CONCRETE)

            entities.add(entity)
        }
    }

    fun move(location: Location, applyOffset: Boolean = true) {
        val offsetLocation = if (applyOffset) applyOffset(location) else location

        for (i in (0 until entities.size)) {
            val entity = entities[i]

            val loc = offsetLocation.clone().add(
                when (axis) {
                    Axis.X -> Vector(i.toDouble() * 0.5, 0.0, 0.0)
                    Axis.Y -> Vector(0.0, i.toDouble() * 0.5, 0.0)
                    Axis.Z -> Vector(0.0, 0.0, i.toDouble() * 0.5)
                }
            )

            entity.teleport(loc)
        }
    }

    fun remove() {
        entities.forEach { it.remove() }
    }

    private fun applyOffset(location: Location, offset: Vector = Vector(0.5, 0.0, 0.5)): Location {
        return location.clone().add(offset)
    }

}
package com.fadedbytes.MCBattleship.mcgame.features

import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import org.bukkit.util.Vector

class HolographicPlaceholder(
    block: Material,
    size: Int,
    val connectors: Boolean,
    initialLocation: Location,
    initialAxis: Axis
) {

    companion object {
        var initialized = false
        lateinit var redTeam: Team
        lateinit var greenTeam: Team
        fun setupTeams() {
            val scoreboard: Scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard ?: throw IllegalStateException("No scoreboard found")

            scoreboard.getTeam("holo_placeholder_red")?.unregister()
            scoreboard.getTeam("holo_placeholder_green")?.unregister()

            redTeam = scoreboard.registerNewTeam("holo_placeholder_red")
            greenTeam = scoreboard.registerNewTeam("holo_placeholder_green")

            redTeam.color = ChatColor.RED
            greenTeam.color = ChatColor.GREEN

            initialized = true
        }
    }

    private val defaultOffset = Vector(0.5, -1.5, 0.5)
    val entities: MutableList<ArmorStand>
    var team: Team? = null
        set(value) {
            field = value
            for (entity in entities) {
                value?.addEntry(entity.uniqueId.toString())
            }
        }

    var material: Material = block
        set(value) {
            field = value
            entities.forEach { it.equipment?.helmet = ItemStack(value) }
        }

    var axis = initialAxis
        set(value) {
            field = value
            move(entities[0].location)
        }

    init {

        if (size < 1) {
            throw IllegalArgumentException("Size must be greater than 0")
        }

        val totalSize = if (connectors) size * 2 - 1 else size
        entities = mutableListOf()

        for (i in 0 until totalSize) {

            val tailOffset = Vector(0.0, 0.0, 0.0)

            when (axis) {
                Axis.X -> tailOffset.x = i * (if (connectors) 0.5 else 1.0)
                Axis.Y -> tailOffset.y = i * (if (connectors) 0.5 else 1.0)
                Axis.Z -> tailOffset.z = i * (if (connectors) 0.5 else 1.0)
            }

            val location = initialLocation.clone().add(tailOffset).add(defaultOffset)
            val entity = initialLocation.world?.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand

            entity.isInvisible = true
            entity.isInvulnerable = true
            entity.isGlowing = true
            entity.setGravity(false)
            entity.setAI(false)
            entity.setMarker(true)

            entity.equipment?.helmet = ItemStack(material)

            entities.add(entity)
        }

        move(initialLocation)

        if (!initialized) setupTeams()
        this.team = greenTeam
    }

    fun move(location: Location, offset: Vector = defaultOffset) {
        for (i in 0 until entities.size) {
            val tailOffset = Vector(0.0, 0.0, 0.0)

            when (axis) {
                Axis.X -> tailOffset.x = i * (if (connectors) 0.5 else 1.0)
                Axis.Y -> tailOffset.y = i * (if (connectors) 0.5 else 1.0)
                Axis.Z -> tailOffset.z = i * (if (connectors) 0.5 else 1.0)
            }

            val location = location.clone().add(tailOffset)
            entities[i].teleport(location.add(offset))
        }
    }

    fun remove() {
        entities.forEach { it.remove() }
    }

}
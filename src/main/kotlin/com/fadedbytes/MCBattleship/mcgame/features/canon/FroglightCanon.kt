package com.fadedbytes.MCBattleship.mcgame.features.canon

import com.fadedbytes.MCBattleship.BattleshipPlugin
import com.fadedbytes.MCBattleship.util.BlockArea
import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.entity.EntityType
import org.bukkit.scheduler.BukkitTask


class FroglightCanon(

    val centerLocation: Location,
    structureLowerOffset: Int = 4,
    structureUpperOffset: Int = 9,
    structureRadius: Int = 4

): Canon {

    private var stopped = false

    private val canonArea = BlockArea(
        centerLocation.clone().add(
            (-structureRadius).toDouble(),
            (-structureLowerOffset).toDouble(),
            (-structureRadius).toDouble()
        ),
        centerLocation.clone().add(
            structureRadius.toDouble(),
            structureUpperOffset.toDouble(),
            structureRadius.toDouble()
        )
    )

    override fun prepareShoot(fromCoords: Location?, afterShoot: () -> Unit) {
        if (fromCoords != null) {
            particleTrail(fromCoords, {
                    activateBeam()
                    switchCanonLights(true) {
                        Bukkit.getScheduler().runTaskLater(BattleshipPlugin.instance, Runnable {
                            shoot()
                        }, 80)
                        Bukkit.getScheduler().runTaskLater(BattleshipPlugin.instance, Runnable {
                            postShoot(afterShoot)
                        }, 120)
                    }
                }
            )
        } else {
            switchCanonLights(true) {switchCanonLights(false)}
        }
    }

    private fun particleTrail(from: Location, onArrival: () -> Unit = {}, speed: Float = 0.8f) {

        val origin = from.clone().toVector()
        val target = centerLocation.clone().toVector()
        val direction = target.subtract(origin).normalize()

        var currentPosition = from

        var task: BukkitTask? = null
        task = Bukkit.getScheduler().runTaskTimer(
            BattleshipPlugin.instance,
            Runnable {

                currentPosition = currentPosition.add(
                    direction.normalize().multiply(
                        speed
                    )
                )

                val dustOptions = DustOptions(Color.fromRGB(255, 174, 0), 1.0f)
                from.world?.spawnParticle(Particle.REDSTONE, currentPosition, 50, dustOptions)

                if (currentPosition.distance(centerLocation) < 1) {
                    task?.cancel()
                    onArrival()
                }
            },
            0L,
            1L
        )
    }

    private fun switchCanonLights(switchOn: Boolean, ticksPerLayer: Long = 2, onSwitch: () -> Unit = {}) {
        if (stopped && switchOn) return

        val blocks = canonArea.getBlocks()

        //filter blocks by layers
        val layers = blocks.groupBy { it.location.y }

        for (i in 0 until layers.size) {
            val layer = layers[layers.keys.elementAt(i)] ?: continue
            Bukkit.getScheduler().runTaskLater(
                BattleshipPlugin.instance,
                Runnable {
                    layer.forEach {
                        if (switchOn) lightBlock(it) else unlightBlock(it)
                    }
                    centerLocation.world?.playSound(centerLocation, if (switchOn) Sound.BLOCK_BEACON_ACTIVATE else Sound.BLOCK_BEACON_DEACTIVATE, 4.0f, (i + 1) * 2.0f / layers.size)
                },
                i.toLong() * ticksPerLayer
            )
        }

        Bukkit.getScheduler().runTaskLater(
            BattleshipPlugin.instance,
            Runnable {
                onSwitch()
            },
            (layers.size - 1).toLong() * ticksPerLayer
        )
    }

    private fun lightBlock(block: Block) {
        if (stopped) return

        block.type = when (block.type) {
            Material.WHITE_TERRACOTTA -> Material.OCHRE_FROGLIGHT
            Material.WHITE_CONCRETE -> Material.PEARLESCENT_FROGLIGHT
            Material.CHAIN -> Material.END_ROD
            else -> return
        }

        if (block.type == Material.END_ROD) {

            val blockData = block.blockData as Directional
            blockData.facing = if (block.getRelative(BlockFace.UP).type == Material.CHAIN) {
                BlockFace.UP
            } else {
                BlockFace.DOWN
            }

            block.blockData = blockData

        }
    }

    private fun unlightBlock(block: Block) {
        block.type = when (block.type) {
            Material.OCHRE_FROGLIGHT -> Material.WHITE_TERRACOTTA
            Material.PEARLESCENT_FROGLIGHT -> Material.WHITE_CONCRETE
            Material.END_ROD -> Material.CHAIN
            else -> return
        }
    }

    private fun activateBeam(then: () -> Unit = {}) {
        centerLocation.block.type = Material.BEACON
        centerLocation.world?.setStorm(true)
        then()
    }

    override fun shoot() {
        centerLocation.world?.spawnEntity(centerLocation, EntityType.LIGHTNING)
        switchCanonLights(false, 0L)
    }

    override fun postShoot(afterShoot: () -> Unit) {
        centerLocation.world?.setStorm(false)
        centerLocation.block.type = Material.AIR

        afterShoot()
    }

    override fun forceStop(afterStop: () -> Unit) {
        this.stopped = true
        postShoot(afterStop)
    }


}
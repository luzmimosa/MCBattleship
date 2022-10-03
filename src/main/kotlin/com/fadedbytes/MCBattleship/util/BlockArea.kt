package com.fadedbytes.MCBattleship.util

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block

class BlockArea(
    val loc1: Location,
    val loc2: Location
): Iterable<Block> {

    init {
        if (loc1.world != loc2.world) {
            throw IllegalArgumentException("Locations must be in the same world")
        }
    }

    constructor(loc1: Block, loc2: Block): this(loc1.location, loc2.location)

    override fun iterator(): Iterator<Block> {
        return BlockAreaIterator(this)
    }

    fun getBlocks(): List<Block> {
        val blocks = mutableListOf<Block>()

        val from = Location(
            loc1.world,
            loc1.x.coerceAtMost(loc2.x),
            loc1.y.coerceAtMost(loc2.y),
            loc1.z.coerceAtMost(loc2.z)
        )
        val to = Location(
            loc1.world,
            loc1.x.coerceAtLeast(loc2.x),
            loc1.y.coerceAtLeast(loc2.y),
            loc1.z.coerceAtLeast(loc2.z)
        )

        for (x in from.blockX..to.blockX) {
            for (y in from.blockY..to.blockY) {
                for (z in from.blockZ..to.blockZ) {
                    loc1.world?.let { blocks.add(it.getBlockAt(x, y, z)) }
                }
            }
        }

        return blocks

    }

    fun getRelativeBlock(x: Int, y: Int, z: Int): Block {
        return loc1.world?.getBlockAt(loc1.blockX + x, loc1.blockY + y, loc1.blockZ + z)!!
    }

    fun getRelativeCoordinatesOf(block: Block): Triple<Int, Int, Int> {
        if (!this.isInArea(block)) {
            throw IllegalArgumentException("Block must be in the area")
        }
        return Triple(block.x - loc1.blockX, block.y - loc1.blockY, block.z - loc1.blockZ)
    }

    fun size(): Int {
        return getBlocks().size
    }

    fun fill(material: Material) {
        for (block in this) {
            block.type = material
        }
    }

    fun isInArea(block: Block): Boolean {
        return this.getBlocks().contains(block)
    }

    fun isInArea(location: Location): Boolean {
        return isInArea(location.block)
    }

}

class BlockAreaIterator(val area: BlockArea): Iterator<Block> {

    var index = 0

    override fun hasNext(): Boolean {
        return area.getBlocks().size > index
    }

    override fun next(): Block {
        return area.getBlocks()[index++]
    }

}
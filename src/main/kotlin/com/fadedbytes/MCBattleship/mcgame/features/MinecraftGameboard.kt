package com.fadedbytes.MCBattleship.mcgame.features

import com.fadedbytes.MCBattleship.game.board.BattleshipGameboard
import com.fadedbytes.MCBattleship.util.BlockArea
import org.bukkit.Location

abstract class MinecraftGameboard(
    val gameboardSize: Int,
    initialLocation: Location
) {

    val gameboardArea: BlockArea = BlockArea(
        initialLocation,
        initialLocation.clone().add(gameboardSize.toDouble() - 1, 0.0, gameboardSize.toDouble() - 1)
    )

    /**
     * Syncs this gameboard with the provided BattleshipGameboard.
     * It must be ensured that the provided BattleshipGameboard is of the same size as this gameboard.
     */
    abstract fun syncWithGameboard(gameboard: BattleshipGameboard)

}
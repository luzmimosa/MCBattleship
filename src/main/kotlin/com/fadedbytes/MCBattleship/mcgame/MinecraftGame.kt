package com.fadedbytes.MCBattleship.mcgame

import com.fadedbytes.MCBattleship.BattleshipPlugin
import com.fadedbytes.MCBattleship.game.BattleshipGame
import com.fadedbytes.MCBattleship.game.GameState
import com.fadedbytes.MCBattleship.game.board.ship.Ship
import com.fadedbytes.MCBattleship.game.board.ship.ShipInfo
import com.fadedbytes.MCBattleship.mcgame.api.listeners.RadarMarkerListener
import com.fadedbytes.MCBattleship.mcgame.features.canon.FroglightCanon
import com.fadedbytes.MCBattleship.mcgame.features.radar.RadarGameboard
import com.fadedbytes.MCBattleship.mcgame.features.radar.RadarPalettes
import com.fadedbytes.MCBattleship.mcgame.features.shipboard.Shipboard
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.Player

class MinecraftGame(
    val player: Player,
    val size: Int,
    radarLocation: Block,
    canonLocation: Block,
    shipboardLocation: Block,
    val onGameEnd: (game: MinecraftGame) -> Unit
) {

    val logicGame = BattleshipGame(size)
    val radar = RadarGameboard(size, radarLocation.location, true)
    val canon = FroglightCanon(canonLocation.location)
    val shipboard = Shipboard(logicGame.bluePlayer.gameboard, shipboardLocation.location)

    lateinit var radarPointer: RadarMarkerListener

    init {
        if (isPlaying(player)) {
            throw IllegalArgumentException("Player is already playing a game")
        }

        playersPlaying[player] = this

        setupRadar()
        startShipPlacement()

    }

    companion object {
        val playersPlaying = mutableMapOf<Player, MinecraftGame>()
        val worldInfoManager = WorldGameManager()

        fun getGame(player: Player): MinecraftGame? {
            return playersPlaying[player]
        }

        fun isPlaying(player: Player): Boolean {
            return playersPlaying.containsKey(player)
        }
    }

    private fun setupRadar() {
        radar.syncWithGameboard(logicGame.bluePlayer.gameboard)
    }

    private fun startShipPlacement() {

        // Randomize computer ships
        logicGame.redPlayer.gameboard.randomizeShips()

        placeNextShip(0)
    }

    private fun placeNextShip(index: Int) {
        if (index >= Ship.values().size) {
            endShipPlacement()
            return
        }

        val ship = Ship.values()[index]
        radarPointer = RadarMarkerListener(
            player,
            radar.gameboardArea,
            ship.size,
            Material.BLACK_CONCRETE,
            { x: Int, y: Int, axis: Axis ->
                val canPlace = logicGame.bluePlayer.gameboard.canPlaceShip(ship, ShipInfo(x, y, axis == Axis.Z))
                radarPointer.material = if (canPlace) Material.BLACK_CONCRETE else Material.RED_CONCRETE
                return@RadarMarkerListener canPlace
            },
            { x: Int, y: Int, axis: Axis ->
                run {
                    val info = ShipInfo(x, y, axis == Axis.Z)
                    if (logicGame.bluePlayer.gameboard.canPlaceShip(ship, info)) {
                        logicGame.bluePlayer.gameboard.placeShip(ship, info)
                        radarPointer.removeHologram()

                        updateAll()
                        shipboard.placeShips()

                        placeNextShip(index + 1)
                    } else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${ChatColor.RED}Cannot place ship here"))
                    }
                }
            }
        )
    }

    private fun endShipPlacement() {
        radar.palette = RadarPalettes.ENEMY.palette
        radar.showShips = false

        nextTurn()
    }

    private fun onPlayerTurn() {
        showEnemyRadar()
        radarPointer = RadarMarkerListener(
            player,
            radar.gameboardArea,
            1,
            Material.SCULK_SHRIEKER,
            {x, y, _ ->
                val canShoot = logicGame.redPlayer.gameboard.canShoot(x, y)
                radarPointer.material = if (canShoot) Material.SCULK_SHRIEKER else Material.BARRIER
                return@RadarMarkerListener canShoot
            },
            {x, y, _ ->
                if (!logicGame.redPlayer.gameboard.canShoot(x, y)) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${ChatColor.RED}Cannot shoot here"))
                    return@RadarMarkerListener
                }

                val hit = logicGame.redPlayer.gameboard.registerFire(x, y)
                radarPointer.removeHologram()

                canon.prepareShoot(radar.gameboardArea.getRelativeBlock(x, 0, y).location) {
                    showEnemyRadar()
                    if (hit) {
                        onPlayerHit()
                    } else {
                        onPlayerMiss()
                    }

                    nextTurn()
                }
            }
        )

    }

    private fun onPlayerHit() {
        player.playSound(player.location, Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 2f)
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${ChatColor.GREEN}Hit!"))
    }

    private fun onPlayerMiss() {
        player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.5f)
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${ChatColor.RED}Miss!"))
    }

    private fun showEnemyRadar() {
        radar.showShips = false
        radar.syncWithGameboard(logicGame.redPlayer.gameboard)
    }

    fun updateAll() {
        setupRadar()
    }

    fun endGame() {
        radar.gameboardArea.fill(Material.AIR)
        shipboard.clear()
        canon.forceStop()
        radarPointer.removeHologram()

        playersPlaying.remove(player)

        onGameEnd(this)
    }

    fun nextTurn() {
        logicGame.nextGameState()

        when (logicGame.gameState) {
            GameState.BLUE_TURN -> onPlayerTurn()
            GameState.RED_TURN -> onEnemyTurn()

            GameState.BLUE_WON -> onPlayerWon()
            GameState.RED_WON -> onComputerWon()
            else -> {}
        }
    }

    private fun onEnemyTurn() {

        var shooted = false

        do {
            val x = (0 until logicGame.bluePlayer.gameboard.size).random()
            val y = (0 until logicGame.bluePlayer.gameboard.size).random()

            if (logicGame.bluePlayer.gameboard.canShoot(x, y)) {
                val hit =  logicGame.bluePlayer.gameboard.registerFire(x, y)
                shooted = true


                Bukkit.getScheduler().runTaskLater(BattleshipPlugin.instance, Runnable {
                    player.playSound(player.location, Sound.ENTITY_WARDEN_SONIC_CHARGE, 1f, 0f)

                    Bukkit.getScheduler().runTaskLater(BattleshipPlugin.instance, Runnable {
                        shipboard.receiveShoot(x, y, hit)
                        player.playSound(player.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1f, 0f)

                        Bukkit.getScheduler().runTaskLater(BattleshipPlugin.instance, Runnable {
                            nextTurn()
                        }, 20L)
                    }, 60L)
                }, (40L until 120L).random())
            }

        } while (!shooted)
    }

    private fun onPlayerWon() {
        player.sendTitle("${ChatColor.GREEN}You won!", "", 10, 40, 10)
        player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)

        endGame()
    }

    private fun onComputerWon() {
        player.sendTitle("${ChatColor.RED}You lost!", "", 10, 40, 10)
        player.playSound(player.location, Sound.ENTITY_WARDEN_AGITATED, 1f, 0f)

        endGame()
    }

}
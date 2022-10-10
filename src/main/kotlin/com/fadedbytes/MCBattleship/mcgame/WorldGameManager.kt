package com.fadedbytes.MCBattleship.mcgame

import com.fadedbytes.MCBattleship.BattleshipPlugin
import org.bukkit.Location
import org.bukkit.World
import org.json.JSONObject
import java.io.File

class WorldGameManager {

    fun getInfo(world: World): PersistentGameInfo? {
        val infoJson = readInfoFile()
        val worldInfo = infoJson.optJSONObject(world.uid.toString()) ?: return null

        try {

            val radar = worldInfo.optJSONObject("radar")
            val canon = worldInfo.optJSONObject("canon")
            val shipboard = worldInfo.optJSONObject("shipboard")

            return PersistentGameInfo(
                Location(
                    world,
                    radar.optDouble("x"),
                    radar.optDouble("y"),
                    radar.optDouble("z")
                ),
                Location(
                    world,
                    canon.optDouble("x"),
                    canon.optDouble("y"),
                    canon.optDouble("z")
                ),
                Location(
                    world,
                    shipboard.optDouble("x"),
                    shipboard.optDouble("y"),
                    shipboard.optDouble("z")
                ),
                world
            )
        } catch (e: Exception) {
            return null
        }
    }

    fun setOrUpdateInfo(info: PersistentGameInfo) {

        val worldsJson = readInfoFile()

        // search if world already exists
        val worldJson = worldsJson.optJSONObject(info.world.uid.toString())

        if (worldJson != null) {
            // remove old world info
            worldsJson.remove(info.world.uid.toString())
        }

        // add new world info
        worldsJson.put(info.world.uid.toString(), generateJsonFor(info))

        saveInfoFile(worldsJson)

    }

    private fun readInfoFile(): JSONObject {
        val configFile = getConfigFile()

        val json = configFile.readText()

        return try {
            JSONObject(json)
        } catch (e: Exception) {
            JSONObject()
        }

    }

    private fun saveInfoFile(json: JSONObject) {
        val configFile = getConfigFile()

        configFile.writeText(json.toString())
    }

    private fun getConfigFile(): File {
        val configFile: File

        val dataFolder = BattleshipPlugin.instance.dataFolder
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        configFile = File(dataFolder, "worlds.json")

        if (!configFile.exists()) {
            configFile.createNewFile()
        }

        return configFile

    }

    private fun generateJsonFor(info: PersistentGameInfo): JSONObject {
        val radarCoords = JSONObject()
        radarCoords.put("x", info.radarLocation.x)
        radarCoords.put("y", info.radarLocation.y)
        radarCoords.put("z", info.radarLocation.z)

        val canonCoords = JSONObject()
        canonCoords.put("x", info.canonLocation.x)
        canonCoords.put("y", info.canonLocation.y)
        canonCoords.put("z", info.canonLocation.z)

        val shipboardCoords = JSONObject()
        shipboardCoords.put("x", info.shipboardLocation.x)
        shipboardCoords.put("y", info.shipboardLocation.y)
        shipboardCoords.put("z", info.shipboardLocation.z)


        val infoJson = JSONObject()
        infoJson.put("radar", radarCoords)
        infoJson.put("canon", canonCoords)
        infoJson.put("shipboard", shipboardCoords)

        return infoJson
    }

}

data class PersistentGameInfo(
    val radarLocation: Location,
    val canonLocation: Location,
    val shipboardLocation: Location,
    val world: World
)
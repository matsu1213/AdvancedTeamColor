package net.azisaba.advancedteamcolor

import com.comphenix.protocol.ProtocolLibrary
import org.bukkit.plugin.java.JavaPlugin

class AdvancedTeamColor : JavaPlugin() {

    companion object{
        lateinit var INSTANCE: AdvancedTeamColor
    }

    private lateinit var teamColorHandler: TeamColorHandler

    override fun onEnable() {
        // Plugin startup logic
        INSTANCE = this
        teamColorHandler = TeamColorHandler()
        ProtocolLibrary.getProtocolManager().addPacketListener(teamColorHandler)
    }

    override fun onDisable() {
        // Plugin shutdown logic

        // Plugin shutdown logic
        ProtocolLibrary.getProtocolManager().removePacketListener(teamColorHandler)
    }
}
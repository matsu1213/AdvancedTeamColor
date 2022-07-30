package net.azisaba.advancedteamcolor

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import net.minecraft.server.v1_12_R1.*
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer

class TeamColorHandler : PacketAdapter(
    AdvancedTeamColor.INSTANCE,
    PacketType.Play.Server.SCOREBOARD_TEAM,
    PacketType.Play.Server.MAP_CHUNK,
    PacketType.Play.Server.TILE_ENTITY_DATA
) {

    override fun onPacketSending(e: PacketEvent) {
        if (e.packetType == PacketType.Play.Server.SCOREBOARD_TEAM) {
            if ((e.packet.modifier.read(7) as Collection<String?>).contains(e.player.name)) {
                val i = e.packet.modifier.read(8) as Int
                if (i == 0 || i == 2) {
                    e.packet.modifier.write(6, 10)
                    val prefix = e.packet.modifier.read(2) as String
                    var greenPrefix = prefix
                    greenPrefix = greenPrefix.replace("§c", "§a")
                    e.packet.modifier.write(2, greenPrefix)
                } else {
                    val ep: EntityPlayer = (e.player as CraftPlayer).getHandle()
                    val team: ScoreboardTeam = ep.getScoreboard().getTeam(e.packet.modifier.read(0) as String)
                    val packet = PacketPlayOutScoreboardTeam(team, 2)
                    if (i == 3) {
                        val prefix = getValue(packet, "c") as String
                        var greenPrefix = prefix
                        greenPrefix = greenPrefix.replace("§c", "§a")
                        setValue(packet, "c", greenPrefix)
                        setValue(packet, "g", 10)
                    }
                    ep.playerConnection.sendPacket(packet)
                }
            }
        } /*else if (e.packetType == PacketType.Play.Server.MAP_CHUNK) {
            val team = e.player.scoreboard.getEntryTeam(e.player.name)
            val l: MutableList<NBTTagCompound> =
                (if (e.packet.modifier.size() == 2) e.packet.modifier.read(1) else e.packet.modifier.read(4)) as MutableList<NBTTagCompound>
            l.forEach(Consumer<NBTTagCompound> { n: NBTTagCompound ->
                val name: String = n.getString("CustomName")
                if (team != null && "§0" + team.name == name && n.getInt("Base") === 1) {
                    n.setInt("Base", 10)
                    l.remove(n)
                    l.add(n)
                }
            })
            if (e.packet.modifier.size() == 2) {
                e.packet.modifier.write(1, l)
            } else {
                e.packet.modifier.write(4, l)
            }
        } */ else if (e.packetType == PacketType.Play.Server.TILE_ENTITY_DATA) {
            val team = e.player.scoreboard.getEntryTeam(e.player.name)
            val n: NBTTagCompound = e.packet.modifier.read(2) as NBTTagCompound
            val nbt: NBTTagCompound = n
            val name: String = nbt.getString("CustomName")
            if (team != null && "§0" + team.name == name && nbt.getInt("Base") === 1) {
                nbt.setInt("Base", 10)
                e.packet.modifier.write(2, nbt)
            }
        }
    }

    fun getValue(instance: Any, name: String): Any? {
        var result: Any? = null
        try {
            val field = instance.javaClass.getDeclaredField(name)
            field.isAccessible = true
            result = field[instance]
            field.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun setValue(instance: Any, name: String, set: Any) {
        try {
            val field = instance.javaClass.getDeclaredField(name)
            field.isAccessible = true
            field[instance] = set
            field.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
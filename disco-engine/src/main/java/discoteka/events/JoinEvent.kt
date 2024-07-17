package discoteka.events

import discoteka.constants.Constants
import discoteka.game.Game
import discoteka.utils.PlayerUtils
import discoteka.utils.registration.listener.DiscoListener
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent

class JoinEvent : DiscoListener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.joinMessage = null

        val player = event.player

        Bukkit.getConsoleSender().sendMessage("§aIP: ${player.address?.address?.hostAddress}")

        PlayerUtils.resetStats(player)

        Game.join(player)
    }
}
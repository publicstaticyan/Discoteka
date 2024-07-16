package discoteka.events

import discoteka.constants.Constants
import discoteka.game.Game
import discoteka.utils.PlayerUtils
import discoteka.utils.registration.listener.DiscoListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class JoinEvent : DiscoListener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.joinMessage = null

        val player = event.player

        PlayerUtils.resetStats(player)

        Game.join(player)
    }
}
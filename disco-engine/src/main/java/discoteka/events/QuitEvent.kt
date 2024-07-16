package discoteka.events

import discoteka.game.Game
import discoteka.utils.registration.listener.DiscoListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerQuitEvent

class QuitEvent : DiscoListener {

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        event.quitMessage = null

        val player = event.player

        Game.leave(player)
    }
}
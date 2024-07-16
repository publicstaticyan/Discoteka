package discoteka.events

import discoteka.game.Game
import discoteka.utils.registration.listener.DiscoListener
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*

class MoveEvent : DiscoListener {

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        val steppedBlock = event.to?.block

        if (Game.isPlaying(player)) {
            if (Objects.nonNull(steppedBlock)) {
                if (steppedBlock!!.type == Material.WATER) {
                    Game.loose(player)
                }
            }
        }
    }
}
package discoteka.events

import discoteka.constants.DiscoConstants
import discoteka.utils.PlayerUtils
import discoteka.utils.registration.listener.DiscoListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class JoinHandler : DiscoListener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        event.joinMessage = null
        PlayerUtils.resetStats(player)

        player.sendMessage("")
        player.sendMessage("§bBem-vindo §a${player.name}§b!")
        player.sendMessage("§bPrepare-se! Pois a ${DiscoConstants.DISCO_COLORFUL_LOGO}§b já vai começar!")
        player.sendMessage("")
    }
}
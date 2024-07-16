package discoteka.events

import discoteka.constants.Constants
import discoteka.enums.GameStage
import discoteka.game.Game
import discoteka.utils.registration.listener.DiscoListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent

class InteractEvent : DiscoListener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val clickedItem = event.item
        val votingStage = Game.getGameStage() == GameStage.WAITING || Game.getGameStage() == GameStage.COUNTDOWN

        if (clickedItem != null) {
            if (votingStage && clickedItem.type == Constants.VOTE_CHEST_ITEM.type) {
                player.performCommand("vote")
            }
        }
    }
}
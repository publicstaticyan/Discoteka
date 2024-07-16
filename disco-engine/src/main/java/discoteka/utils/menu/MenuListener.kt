package discoteka.utils.menu

import discoteka.utils.registration.listener.DiscoListener
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent

class MenuListener : DiscoListener {
    @EventHandler
    fun menuClick(e: InventoryClickEvent) {
        val menu = e.inventory.holder

        if (menu is DiscoMenu) {
            if (e.currentItem == null) {
                return
            }
            menu.handleInteraction(e)
        }
    }
}

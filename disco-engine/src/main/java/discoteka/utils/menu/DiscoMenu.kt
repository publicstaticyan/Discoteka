package discoteka.utils.menu

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class DiscoMenu(protected val holder: Player, private val slots: Int, private val title: String) : InventoryHolder {
    private lateinit var inventory: Inventory
    open fun open() {
        inventory = Bukkit.createInventory(this, slots, title)
        setDefaultItems()
        holder.openInventory(inventory)
    }

    abstract fun setDefaultItems()
    abstract fun handleInteraction(e: InventoryClickEvent)
    override fun getInventory(): Inventory {
        return inventory
    }
}

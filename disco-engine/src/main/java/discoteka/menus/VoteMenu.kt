package discoteka.menus

import discoteka.enums.Music
import discoteka.game.Vote
import discoteka.utils.StackBuilder
import discoteka.utils.menu.DiscoMenu
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag

class VoteMenu(holder: Player) : DiscoMenu(holder, 9, "Vote na sua musica! =D") {
    override fun setDefaultItems() {
        for (music in Music.values()) {
            this.inventory.setItem(music.ordinal, music.item)
        }

        val votedMusic = Vote.getVote(holder)

        if (votedMusic != null) {
            val enchantedItem = StackBuilder(this.inventory.getItem(votedMusic.ordinal)!!)
                .addEnchant(Enchantment.POWER, 1)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .toItemStack()

            this.inventory.setItem(votedMusic.ordinal, enchantedItem)
        }
    }

    override fun handleInteraction(e: InventoryClickEvent) {
        val player = e.whoClicked as Player

        e.isCancelled = true

        Vote.vote(player, e.slot)

        player.closeInventory()
    }
}

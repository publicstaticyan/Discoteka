package discoteka.utils

import org.bukkit.entity.Player
import org.bukkit.util.Vector

object PlayerUtils {
    fun resetStats(player: Player) {
        player.totalExperience = 0
        player.level = 0
        player.exp = 0.0f
        player.health = 20.0
        player.foodLevel = 20
        player.saturation = 5.0f
        player.inventory.clear()
        player.velocity = Vector()
    }
}
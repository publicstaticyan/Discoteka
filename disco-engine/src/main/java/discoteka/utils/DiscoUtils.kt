package discoteka.utils

import discoteka.constants.Constants
import discoteka.game.Arena
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

object DiscoUtils {
    fun sendMessage(message: String) {
        Bukkit.broadcastMessage("${Constants.DISCO_COLORFUL_LOGO}§f $message")
    }

    fun getRandomConcreteColor(usedColors: MutableList<Material>): ItemStack {
        val random = Random()
        var color: ItemStack

        do {
            color = Constants.COLORS[random.nextInt(Constants.COLORS.size)].toItemStack()
        } while (usedColors.contains(color.type))

        usedColors.add(color.type)

        if (usedColors.size >= Constants.COLORS.size - 1) {
            usedColors.clear()
        }

        return color;
    }
}
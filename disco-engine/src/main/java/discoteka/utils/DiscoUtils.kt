package discoteka.utils

import discoteka.constants.DiscoConstants
import org.bukkit.Bukkit

object DiscoUtils {
    fun broadcast(message: String) {
        Bukkit.broadcastMessage("${DiscoConstants.DISCO_COLORFUL_LOGO}§b:§f $message")
    }
}
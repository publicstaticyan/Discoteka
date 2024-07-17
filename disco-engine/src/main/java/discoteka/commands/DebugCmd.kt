package discoteka.commands

import discoteka.utils.registration.command.DiscoCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DebugCmd : DiscoCommand(
    commandName = "debug",
    permission = true
) {
    override fun onCommand(sender: CommandSender?, args: Array<String>?) {
        val player = sender as Player

        when (args?.get(0)) {
            "sound" -> {
                player.playSound(player.location, args[1], 15.0f, 15.0f)
            }
        }
    }
}
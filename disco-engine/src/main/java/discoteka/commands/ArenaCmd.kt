package discoteka.commands

import discoteka.game.Arena
import discoteka.utils.registration.command.DiscoCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArenaCmd : DiscoCommand(
    commandName = "arena",
    permission = true
) {
    override fun onCommand(sender: CommandSender?, args: Array<String>?) {
        val player = sender as Player
        when (args?.get(0)) {
            "spawn" -> {
                Arena.spawn()
            }
            "destroy" -> {
                Arena.destroy()
            }
            "center" -> {
                player.teleport(Arena.getCenter())
            }
        }
    }
}
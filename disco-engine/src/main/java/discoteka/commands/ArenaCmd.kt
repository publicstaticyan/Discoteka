package discoteka.commands

import discoteka.game.Arena
import discoteka.utils.registration.command.DiscoCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class ArenaCmd : DiscoCommand(
    commandName = "arena",
    permission = true
) {
    override fun onCommand(sender: CommandSender?, args: Array<String>?) {
        val player = sender as Player
        when (args?.get(0)) {
            "spawn" -> {
                if (args.size > 1) {
                    Arena.modifyProperties(args[1].toInt(), args[2].toInt(), args[3].toInt())
                }

                Arena.spawn()
            }
            "purge" -> {
                Arena.purgeBlocks(player.inventory.itemInMainHand.type)
            }
            "destroy" -> {
                Arena.destroy()
            }
            "center" -> {
                player.teleport(Arena.center)
            }
        }
    }
}
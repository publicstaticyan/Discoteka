package discoteka.commands

import discoteka.game.Vote
import discoteka.menus.VoteMenu
import discoteka.utils.registration.command.DiscoCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VoteCmd : DiscoCommand(
    commandName = "vote",
    permission = false
) {
    override fun onCommand(sender: CommandSender?, args: Array<String>?) {
        val player = sender as Player

        if (args?.size == 0) {
            VoteMenu(player).open()
            return
        }

        Vote.vote(player, args!![1].toInt())
    }
}
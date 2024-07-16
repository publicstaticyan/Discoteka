package discoteka.commands

import discoteka.game.Game
import discoteka.utils.registration.command.DiscoCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GameCmd : DiscoCommand(
    commandName = "game",
    permission = true
) {
    override fun onCommand(sender: CommandSender?, args: Array<String>?) {
        val player = sender as Player

        when (args?.get(0)) {
            "start" -> {
                Game.beginCountdown()
            }
            "forcestart" -> {
                Game.beginGame()
            }
            "stop" -> {
                Game.endGame()
            }
            "join" -> {
                Game.join(player)
            }
            "leave" -> {
                Game.leave(player)
            }
            "info" -> {
                sender.sendMessage("§bGame: §e$Game")
            }
        }
    }
}
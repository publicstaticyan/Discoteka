package discoteka.commands

import discoteka.enums.MessageType
import discoteka.utils.registration.command.DiscoCommand
import discoteka.vo.MessageVO
import discoteka.websocket.WebSocketServer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlayCmd : DiscoCommand(
    commandName = "play",
    permission = false
) {
    override fun onCommand(sender: CommandSender?, args: Array<String>?) {
        val link = args?.get(0)
        val player = sender as Player
        WebSocketServer.notifySessions(player.name, MessageVO(MessageType.COMMAND, mapOf("play" to link!!)))
        player.sendMessage("§aPlaying the youtube link: $link")
    }
}
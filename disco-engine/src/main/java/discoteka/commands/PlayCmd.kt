package discoteka.commands

import discoteka.enums.MessageType
import discoteka.enums.Music
import discoteka.enums.Playback
import discoteka.handlers.WebPlayerHandler
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
        val idx = args?.get(0)?.toInt()
        val player = sender as Player

        val music = Music.values()[idx!!]

        WebPlayerHandler.link(player, music.url)
        WebPlayerHandler.command(player, Playback.PLAY)

        player.sendMessage("§aPlaying the youtube link: ${music.title}")
    }
}
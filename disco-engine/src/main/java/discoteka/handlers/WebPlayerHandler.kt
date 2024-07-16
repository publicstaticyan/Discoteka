package discoteka.handlers

import discoteka.enums.MessageType
import discoteka.enums.Playback
import discoteka.vo.MessageVO
import discoteka.websocket.WebSocketServer
import org.bukkit.entity.Player

object WebPlayerHandler {
    fun link(player: Player, url: String) {
        if (!WebSocketServer.isSessionRegistered(player.name)) return

        val message = MessageVO(
            MessageType.LINK,
            mapOf("url" to url)
        )

        WebSocketServer.notifySessions(player.name, message)
    }

    fun reset() {
        val message = MessageVO(
            MessageType.LINK,
            mapOf("url" to "")
        )

        WebSocketServer.notifyAllSessions(message)
    }

    fun command(player: Player, command: Playback) {
        if (!WebSocketServer.isSessionRegistered(player.name)) return

        val message = MessageVO(
            MessageType.COMMAND,
            mapOf("command" to command.name.lowercase())
        )

        WebSocketServer.notifySessions(player.name, message)
    }
}
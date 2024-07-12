package discoteka.websocket

import discoteka.enums.MessageType
import discoteka.utils.Mapper
import discoteka.vo.MessageVO
import jakarta.websocket.OnClose
import jakarta.websocket.OnMessage
import jakarta.websocket.OnOpen
import jakarta.websocket.Session
import jakarta.websocket.server.ServerEndpoint
import org.bukkit.Bukkit
import java.util.*

// TODO create proper encoders
@ServerEndpoint("/ws")
class WebSocketEndpoint {

    @OnOpen
    fun onOpen(session: Session) {
        Bukkit.getConsoleSender().sendMessage("New connection established! Session: [${session.id}]")
    }

    @OnClose
    fun onClose(session: Session) {
        Bukkit.getConsoleSender().sendMessage("Connection closed! Session: [${session.id}]")
        WebSocketServer.unregisterSession(session)
    }

    @OnMessage
    fun onMessage(json: String, session: Session) {
        // TODO create a singleton


        val message = Mapper.objectMapper.readValue(json, MessageVO::class.java)

        when (message.type) {
            MessageType.PING -> {
                val response = MessageVO(MessageType.PING, mapOf("reply" to "pong"))
                session.basicRemote.sendText(Mapper.objectMapper.writeValueAsString(response))
            }
            MessageType.PLAYER_VALIDATION -> {
                val playerName = message.payload["playerName"] as String
                val isValidPlayer = Bukkit.getPlayerExact(playerName) != null
                val response = MessageVO(MessageType.PLAYER_VALIDATION, mapOf("reply" to isValidPlayer))

                session.basicRemote.sendText(Mapper.objectMapper.writeValueAsString(response))

                if (isValidPlayer) {
                    WebSocketServer.registerSession(playerName, session)
                }
            }
            MessageType.COMMAND -> {

            }
        }

        Bukkit.getConsoleSender().sendMessage("§6WebSocket message received: [Message]: $message - [Session]: ${session.id}")
    }
}
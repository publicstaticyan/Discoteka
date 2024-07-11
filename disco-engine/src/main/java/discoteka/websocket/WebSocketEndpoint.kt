package discoteka.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import discoteka.enums.MessageType
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

    private val sessions = mutableMapOf<String, Session>()

    @OnOpen
    fun onOpen(session: Session) {
        Bukkit.getConsoleSender().sendMessage("New connection established! Session: [${session.id}]")
    }

    @OnClose
    fun onClose(session: Session) {
        Bukkit.getConsoleSender().sendMessage("Connection closed! Session: [${session.id}]")
    }

    @OnMessage
    fun onMessage(json: String, session: Session) {
        // TODO create a singleton
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(kotlinModule())

        val message = objectMapper.readValue(json, MessageVO::class.java)

        when (message.type) {
            MessageType.PING -> {
                val response = MessageVO(MessageType.PING, mapOf("reply" to "pong"))
                session.basicRemote.sendText(objectMapper.writeValueAsString(response))
            }
            MessageType.PLAYER_VALIDATION -> {
                val playerName = message.payload["playerName"] as String
                val isValidPlayer = Bukkit.getPlayerExact(playerName) != null
                val response = MessageVO(MessageType.PLAYER_VALIDATION, mapOf("reply" to isValidPlayer))
                session.basicRemote.sendText(objectMapper.writeValueAsString(response))
            }
            MessageType.COMMAND -> {

            }
        }

        Bukkit.getConsoleSender().sendMessage("§6WebSocket message received: [Message]: $message - [Session]: ${session.id}")
    }
}
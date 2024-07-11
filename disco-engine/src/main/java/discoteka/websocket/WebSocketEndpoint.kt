package discoteka.websocket

import jakarta.websocket.OnClose
import jakarta.websocket.OnMessage
import jakarta.websocket.OnOpen
import jakarta.websocket.Session
import jakarta.websocket.server.ServerEndpoint
import org.bukkit.Bukkit

@ServerEndpoint("/ws")
class WebSocketEndpoint {
    @OnOpen
    fun onOpen(session: Session) {
        Bukkit.getConsoleSender().sendMessage("New connection established! Session: [${session.id}]")
    }
    @OnClose
    fun onClose(session: Session) {
        Bukkit.getConsoleSender().sendMessage("Connection closed! Session: [${session.id}]")
    }
    @OnMessage
    fun onMessage(message: String, session: Session) {
        if (message == "ping") {
            session.basicRemote.sendText("pong")
            return
        }

        Bukkit.broadcastMessage("WebSocket message: $message - Session: ${session.id}")
    }
}
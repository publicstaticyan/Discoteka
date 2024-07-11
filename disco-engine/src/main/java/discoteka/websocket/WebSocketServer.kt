package discoteka.websocket

import jakarta.servlet.ServletContext
import jakarta.websocket.server.ServerContainer
import org.bukkit.Bukkit
import org.eclipse.jetty.ee10.servlet.ServletContextHandler
import org.eclipse.jetty.ee10.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer
import org.eclipse.jetty.server.Server
import java.net.BindException

object WebSocketServer {

    private lateinit var server: Server

    private var port = 8080

    fun start() {
        try {
            createServer()
        } catch (ex: Exception) {
            // TODO increase ports until connection is established
            port++
            Bukkit.getConsoleSender().sendMessage("§c[SOCKET] Failed! Retrying on port $port")
            createServer()
        }
    }

    private fun createServer() {
        server = Server(port)

        Bukkit.getConsoleSender().sendMessage("§6[SOCKET] Creating WebSocket server on port $port")

        val servletContext = ServletContextHandler(ServletContextHandler.SESSIONS)
        servletContext.contextPath = "/"
        server.handler = servletContext

        JakartaWebSocketServletContainerInitializer.configure(
            servletContext
        ) { _: ServletContext?, wsContainer: ServerContainer ->
            wsContainer.addEndpoint(
                WebSocketEndpoint::class.java
            )
            wsContainer.defaultMaxSessionIdleTimeout = 0
        }

        server.start()
        Bukkit.getConsoleSender().sendMessage("§a[SOCKET] WebSocket server started on port $port")
        // join() makes the main thread wait for the parallel thread to end (which never ends)
//        server.join()
    }
}
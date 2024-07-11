package discoteka

import discoteka.managers.GameManager
import discoteka.utils.registration.command.CommandLoader
import discoteka.utils.registration.listener.ListenerLoader
import discoteka.websocket.WebSocketServer
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class DiscoEngine : JavaPlugin() {

    override fun onLoad() {
        Bukkit.getConsoleSender().sendMessage("§6[DISCOTEKA] Engine started loading...")
    }

    override fun onEnable() {
        Bukkit.getConsoleSender().sendMessage("§a[DISCOTEKA] Engine successfully started!")

        CommandLoader.registerCommands()
        ListenerLoader.registerListeners()

        GameManager.start()
        WebSocketServer.start()
    }

    companion object {
        fun getInstance(): DiscoEngine {
            return getPlugin(DiscoEngine::class.java)
        }
    }
}

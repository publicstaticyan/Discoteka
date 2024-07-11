package discoteka.utils.registration.listener

import discoteka.DiscoEngine
import discoteka.utils.ClassGetter
import org.bukkit.Bukkit
import org.bukkit.event.Listener


object ListenerLoader {
    fun registerListeners() {
        var i = 0
        val srcPackage = ListenerLoader::class.java.getPackage().name.substring(
            0,
            ListenerLoader::class.java.getPackage().name.indexOf('.')
        )
        for (baseListener in ClassGetter.getClassesForPackage(DiscoEngine.getInstance(), srcPackage)) {
            if (DiscoListener::class.java.isAssignableFrom(baseListener) && baseListener != DiscoListener::class.java) {
                try {
                    val listener: DiscoListener = baseListener.getConstructor().newInstance() as DiscoListener
                    Bukkit.getPluginManager().registerEvents(listener as Listener, DiscoEngine.getInstance())
                } catch (e: Exception) {
                    e.printStackTrace()
                    print("[ListenerLoader] Error when registering the listener " + baseListener.simpleName)
                }
                i++
            }
        }
        DiscoEngine.getInstance().logger.info("[ListenerLoader] $i listeners loaded")
    }
}
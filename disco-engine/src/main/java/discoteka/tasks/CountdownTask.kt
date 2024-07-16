package discoteka.tasks

import discoteka.DiscoEngine
import discoteka.game.Game
import discoteka.utils.DiscoUtils
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

object CountdownTask {

    const val WAITING_COUNTDOWN_TIME = 30
    private lateinit var task: BukkitTask

    fun start() {
        task = object : BukkitRunnable() {

            private var count = WAITING_COUNTDOWN_TIME

            override fun run() {
                when (count)
                {
                    120, 60 -> {
                        DiscoUtils.sendMessage("§aReady to dance?! Começando em §b${count / 60}§a minuto(s)!")
                    }
                    30, 15, 10 -> {
                        DiscoUtils.sendMessage("§6Começando a festa em §b$count§6 segundos! XD")
                    }
                    5, 4, 3, 2, 1 -> {
                        DiscoUtils.sendMessage("§bHey ho, let's go! Vamos em começar em §a$count§b...")
                    }
                    0 -> {
                        DiscoUtils.sendMessage("§aIT'S DANCING TIME =D! Boa partida!")
                        Game.beginGame()
                        stop()
                    }
                }
                count--
            }
        }.runTaskTimer(DiscoEngine.getInstance(), 0, 20)
    }

    fun stop() {
        task.cancel()
    }
}
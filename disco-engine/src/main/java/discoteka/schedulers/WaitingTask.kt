package discoteka.schedulers

import discoteka.constants.DiscoConstants
import discoteka.game.GameState
import discoteka.utils.DiscoUtils
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class WaitingTask(gameState: GameState) : BukkitRunnable() {

    private var count = DiscoConstants.WAITING_COUNTDOWN_TIME

    override fun run() {
        count--

        when (count) {
            120, 60 -> {
                DiscoUtils.broadcast("§aReady to dance?! Começando em §b${count / 60}§a minuto(s)!")
            }
            30, 15, 10 -> {
                DiscoUtils.broadcast("§6Começando a festa em §b$count§6 segundos! XD")
            }
            5, 4, 3, 2, 1 -> {
                DiscoUtils.broadcast("§bHey ho, let's go! Vamos em começar em §a$count§b...")
            }
            0 -> {
                DiscoUtils.broadcast("§aIT'S DANCING TIME =D! Boa partida!")
                // TODO: Trigger start event
                cancel()
            }
        }
    }
}
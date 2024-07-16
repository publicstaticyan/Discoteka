package discoteka.game

import discoteka.DiscoEngine
import discoteka.constants.Constants
import discoteka.enums.GameStage
import discoteka.enums.Music
import discoteka.enums.Playback
import discoteka.handlers.WebPlayerHandler
import discoteka.tasks.CountdownTask
import discoteka.utils.DiscoUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.ArrayList
import java.util.Random

object Game {

    private const val INITIAL_BOSS_BAR_MESSAGE = "§b§lAGUARDANDO A ${Constants.DISCO_COLORFUL_LOGO} §b§lCOMEÇAR!"

    private var gameStage = GameStage.WAITING
    private var playerSet = mutableSetOf<Player>()
    private var speedGap = 3.0
    private val countdownTask = CountdownTask
    private val bossBar: BossBar = Bukkit.createBossBar(INITIAL_BOSS_BAR_MESSAGE, BarColor.BLUE, BarStyle.SOLID)
    private val random = Random()

    fun join(player: Player) {
        if (gameStage == GameStage.WAITING) {
            player.inventory.setItem(0, Constants.VOTE_CHEST_ITEM)
            player.sendMessage("${Constants.DISCO_COLORFUL_LOGO} §eBem-vindo §a${player.name}§e, a festa já vai começar!")
            bossBar.addPlayer(player)
            tryToBeginCountdown()
        }

        if (gameStage == GameStage.WAITING || gameStage == GameStage.COUNTDOWN) {
            DiscoUtils.sendMessage("§b${player.name}§e entrou na festa! §3(§6${Bukkit.getOnlinePlayers().size}§3/§6${Constants.PLAYER_MIN}§3)")
        }
    }

    private fun tryToBeginCountdown() {
        if (Bukkit.getOnlinePlayers().size >= Constants.PLAYER_MIN) {
            beginCountdown()
        }
    }

    fun beginCountdown() {
        gameStage = GameStage.COUNTDOWN
        countdownTask.start()
    }

    fun forceStart() {
        countdownTask.stop()

    }

    fun beginGame() {
        gameStage = GameStage.PLAYING

        DiscoUtils.sendMessage("§aVamos dançar!")

        for (player in Bukkit.getOnlinePlayers()) {
            playerSet.add(player)
            player.teleport(Arena.center)
        }

        startMusic()
        runGame()
    }

    private fun runGame() {
        // Just for record: nextRound() calls do not cause overhead, when the runnable calls the function
        // again, the last one returns and continues the line to nextGame()
        nextRound()
//        resetGame()
    }

    private fun nextRound() {
        if (playerSet.isEmpty()) {
            return
        }

        bossBar.setTitle("§e§lCOMEÇANDO ROUND...")

        val usedConcretes = Arena.spawn()
        val chosenConcrete = usedConcretes[random.nextInt(usedConcretes.size)]

        for (player in playerSet) {
            player.inventory.setItem(4, chosenConcrete)
        }

        object : BukkitRunnable() {
            override fun run() {
                bossBar.setTitle(chosenConcrete.itemMeta?.itemName)
            }
        }.runTaskLater(DiscoEngine.getInstance(), 20 * 1)

        object : BukkitRunnable() {
            override fun run() {
                startCountdown(chosenConcrete)
            }
        }.runTaskLater(DiscoEngine.getInstance(), 20 * 2)
    }

    private fun startCountdown(chosenConcrete: ItemStack) {
        object : BukkitRunnable() {
            var count = 3
            override fun run() {
                if (count == 0) {
                    cancel()
                    purgeBlocks(chosenConcrete.type)
                    return
                }

                for (player in Bukkit.getOnlinePlayers()) {
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 10.0F, (0.5 * count).toFloat())
                }

                bossBar.setTitle(chosenConcrete.itemMeta?.itemName + " §e§l${count}...")

                bossBar.progress = (count.toDouble() - 1.0) / 2.0
                count--
            }
        }.runTaskTimer(DiscoEngine.getInstance(), 0, (20 * speedGap).toLong())
    }

    private fun purgeBlocks(chosenConcrete: Material) {
        Arena.purgeBlocks(chosenConcrete)
        bossBar.setTitle("§c§lPARADOS!")

        for (player in playerSet) {
            WebPlayerHandler.command(player, Playback.PAUSE)
        }

        object : BukkitRunnable() {
            override fun run() {
                bossBar.setTitle("§a§lVOCÊ CONSEGUIU!")

                for (player in playerSet) {
                    WebPlayerHandler.command(player, Playback.ALIVE)
                    WebPlayerHandler.command(player, Playback.PLAY)
                }

                if (speedGap > 1.0) {
                    speedGap -= 0.2
                }
            }
        }.runTaskLater(DiscoEngine.getInstance(), 20 * 3)

        object : BukkitRunnable() {
            override fun run() {
                nextRound()
            }
        }.runTaskLater(DiscoEngine.getInstance(), 20 * 5)
    }

    fun leave(player: Player) {
        if (gameStage == GameStage.PLAYING) {
            loose(player)
        }

        if (gameStage == GameStage.COUNTDOWN) {
            val playerUnderMin = Bukkit.getOnlinePlayers().size < Constants.PLAYER_MIN

            if (playerUnderMin && gameStage == GameStage.COUNTDOWN) {
                countdownTask.stop()
                gameStage = GameStage.WAITING
            }
        }
    }

    fun loose(player: Player) {
        if (playerSet.contains(player)) {
            playerSet.remove(player)
            player.inventory.clear()
            player.sendMessage("§b[${Constants.DISCO_COLORFUL_LOGO}§b] §c${player.name}§e perdeu!")

            if (playerSet.size == 1 || playerSet.size == 0) {
                endGame()
            }
        }
    }

    fun endGame() {
        gameStage = GameStage.FINISHED

        val winner = playerSet.single()
        DiscoUtils.sendMessage("§a" + winner.name + "§e venceu!!")

        object : BukkitRunnable() {
            override fun run() {
                playerSet.remove(winner)
                winner.teleport(Bukkit.getWorld("world")!!.spawnLocation)
                gameStage = GameStage.WAITING
                tryToBeginCountdown()
            }
        }.runTaskLater(DiscoEngine.getInstance(), 20 * 5)
    }

    private fun resetGame() {
        playerSet.clear()
        speedGap = 3.0
        WebPlayerHandler.reset()
    }

    private fun startMusic() {
        var chosenMusic = Vote.getMostVotedMusic()

        if (chosenMusic == null) {
            chosenMusic = Music.values()[Random().nextInt(Music.values().size)]
        }

        for (player in playerSet) {
            WebPlayerHandler.link(player, chosenMusic.url)
            WebPlayerHandler.command(player, Playback.PLAY)
        }

        DiscoUtils.sendMessage("§6========================================")
        DiscoUtils.sendMessage("§eTocando §a${chosenMusic.title} §6de §a${chosenMusic.author}")
        DiscoUtils.sendMessage("§6========================================")
    }

    fun isPlaying(player: Player): Boolean {
        return playerSet.contains(player)
    }

    fun getGameStage(): GameStage {
        return gameStage
    }

    override fun toString(): String {
        return "Game(" +
                "gameStage=" + gameStage +
                ", playerSet=" + playerSet.toString() +
                ")"
    }
}
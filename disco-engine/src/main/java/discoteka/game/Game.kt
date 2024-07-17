package discoteka.game

import discoteka.DiscoEngine
import discoteka.constants.Constants
import discoteka.enums.GameStage
import discoteka.enums.Music
import discoteka.enums.Playback
import discoteka.handlers.WebPlayerHandler
import discoteka.tasks.CountdownTask
import discoteka.utils.DiscoUtils
import discoteka.utils.FireworkBuilder
import org.bukkit.*
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*

object Game {

    private const val INITIAL_BOSS_BAR_MESSAGE = "§6§lAGUARDANDO A ${Constants.DISCO_COLORFUL_LOGO} §6§lCOMEÇAR!"

    private var gameStage = GameStage.WAITING
    private var playerSet = mutableSetOf<Player>()
    private var speedGap = 3.0
    private val countdownTask = CountdownTask
    private val bossBar: BossBar = Bukkit.createBossBar(INITIAL_BOSS_BAR_MESSAGE, BarColor.BLUE, BarStyle.SOLID)
    private val aliveBossBar: BossBar = Bukkit.createBossBar("§a§lVOCÊ CONSEGUIU", BarColor.GREEN, BarStyle.SOLID)
    private val random = Random()
    private var round = 0

    var chosenMusic: Music? = null

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
        beginGame()
    }

    fun beginGame() {
        gameStage = GameStage.PLAYING

        DiscoUtils.sendMessage("§aVamos dançar!")

        Bukkit.getOnlinePlayers().forEach {
            playerSet.add(it)
            it.inventory.clear()
            it.teleport(Arena.center)
        }

        startMusic()
        runGame()
    }

    private fun runGame() {
        // Just for record: nextRound() calls do not cause overhead, when the runnable calls the function
        // again, the last one returns and continues the line to the next execution
        nextRound()
    }

    private fun nextRound() {
        if (gameStage != GameStage.PLAYING) {
            return
        }

        round++

        bossBar.color = BarColor.YELLOW
        bossBar.setTitle("§e§lCOMEÇANDO ROUND ${round}...")

        val pair = Constants.getRandomPair(round)
        Arena.modifyProperties(pair.first, pair.second)

        val usedConcretes = Arena.spawn()
        val chosenConcrete = usedConcretes[random.nextInt(usedConcretes.size)]

        playerSet.forEach {
            it.inventory.setItem(4, chosenConcrete)
        }

        object : BukkitRunnable() {
            override fun run() {
                if (gameStage == GameStage.PLAYING) {
                    bossBar.setTitle(chosenConcrete.itemMeta?.displayName)
                    bossBar.color = BarColor.BLUE
                }
            }
        }.runTaskLater(DiscoEngine.getInstance(), 20 * 1)

        object : BukkitRunnable() {
            override fun run() {
                if (gameStage == GameStage.PLAYING) {
                    startCountdown(chosenConcrete)
                }
            }
        }.runTaskLater(DiscoEngine.getInstance(), 20 * 2)
    }

    private fun startCountdown(chosenConcrete: ItemStack) {
        object : BukkitRunnable() {
            var count = 3
            override fun run() {
                if (gameStage != GameStage.PLAYING) {
                    cancel()
                    return
                }

                if (count == 0) {
                    cancel()
                    purgeBlocks(chosenConcrete.type)
                    return
                }

                Bukkit.getOnlinePlayers().forEach {
                    it.playSound(it.location, Sound.BLOCK_NOTE_BLOCK_PLING, 10.0F, (0.5 * count).toFloat())
                }

                bossBar.setTitle(chosenConcrete.itemMeta?.displayName + " §e§l${count}...")

                bossBar.progress = (count.toDouble() - 1.0) / 2.0
                count--
            }
        }.runTaskTimer(DiscoEngine.getInstance(), 0, (20 * speedGap).toLong())
    }

    private fun purgeBlocks(chosenConcrete: Material) {
        Arena.purgeBlocks(chosenConcrete)
        bossBar.setTitle("§c§lPARADOS!")
        bossBar.color = BarColor.RED
        bossBar.progress = 1.0

        Bukkit.getOnlinePlayers().forEach {
            WebPlayerHandler.command(it, Playback.PAUSE)
        }

        object : BukkitRunnable() {
            override fun run() {
                if (gameStage == GameStage.PLAYING) {
                    playerSet.forEach {
                        bossBar.removePlayer(it)
                        aliveBossBar.addPlayer(it)
                        WebPlayerHandler.command(it, Playback.ALIVE)
                    }

                    Bukkit.getOnlinePlayers().forEach {
                        WebPlayerHandler.command(it, Playback.PLAY)
                    }

                    if (speedGap > 0.5) {
                        speedGap -= 0.2
                    }
                }
            }
        }.runTaskLater(DiscoEngine.getInstance(), 20 * 3)

        object : BukkitRunnable() {
            override fun run() {
                if (gameStage == GameStage.PLAYING) {
                    playerSet.forEach {
                        bossBar.addPlayer(it)
                        aliveBossBar.removePlayer(it)
                    }
                }

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
            Bukkit.getWorld("world")!!.strikeLightningEffect(player.location)
            playerSet.remove(player)
            player.inventory.clear()
            player.teleport(Bukkit.getWorld("world")!!.spawnLocation)
            player.sendMessage("${Constants.DISCO_COLORFUL_LOGO} §c${player.name}§e perdeu!")

            Bukkit.getOnlinePlayers().forEach {
                WebPlayerHandler.command(it, Playback.LOOSE)
            }

            if (playerSet.size == 1) {
                endGame()
            }
        }
    }

    fun endGame() {
        gameStage = GameStage.FINISHED
        Arena.spawn()

        val winner = playerSet.single()
        winner.teleport(Arena.center)
        DiscoUtils.sendMessage("§a" + winner.name + "§e venceu!!")
        bossBar.color = BarColor.PURPLE
        bossBar.progress = 1.0
        bossBar.setTitle("§a§l" + winner.name + "§e§l VENCEU!")

        Bukkit.getOnlinePlayers().forEach {
            WebPlayerHandler.command(it, Playback.WINNER)
        }

        val task = object : BukkitRunnable() {
            override fun run() {
                spawnFireworks(winner)
            }
        }.runTaskTimer(DiscoEngine.getInstance(), 0, 10)

        object : BukkitRunnable() {
            override fun run() {
                task.cancel()
                winner.teleport(Bukkit.getWorld("world")!!.spawnLocation)
                resetGame()
            }
        }.runTaskLater(DiscoEngine.getInstance(), 20 * 10)
    }

    private fun resetGame() {
        round = 0
        playerSet.clear()
        speedGap = 3.0
        gameStage = GameStage.WAITING
        bossBar.progress = 1.0
        bossBar.setTitle(INITIAL_BOSS_BAR_MESSAGE)

        tryToBeginCountdown()
        WebPlayerHandler.reset()

        Bukkit.getOnlinePlayers().forEach {
            it.inventory.clear()
            it.inventory.setItem(0, Constants.VOTE_CHEST_ITEM)
        }
    }

    private fun startMusic() {
        chosenMusic = Vote.getMostVotedMusic()

        if (chosenMusic == null) {
            chosenMusic = Music.values()[Random().nextInt(Music.values().size)]
        }

        Bukkit.getOnlinePlayers().forEach {
            WebPlayerHandler.link(it, chosenMusic!!.url)
            WebPlayerHandler.command(it, Playback.PLAY)
        }

        DiscoUtils.sendMessage("§6========================================")
        DiscoUtils.sendMessage("§eTocando §a${chosenMusic!!.title} §6de §a${chosenMusic!!.author}")
        DiscoUtils.sendMessage("§6========================================")
    }

    private fun spawnFireworks(winner: Player) {
        val r = random.nextInt(256)
        val g = random.nextInt(256)
        val b = random.nextInt(256)

        val effects = FireworkEffect.builder()
            .withTrail()
            .withFlicker()
            .with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().size)])
            .withColor(Color.fromRGB(r, g, b))
            .build()

        FireworkBuilder(winner.location).effects(effects).power(2)
    }

    fun isPlaying(player: Player): Boolean {
        return playerSet.contains(player)
    }

    fun getGameStage(): GameStage {
        return gameStage
    }

    fun hasStarted(): Boolean {
        return gameStage == GameStage.PLAYING
    }

    override fun toString(): String {
        return "Game(" +
                "gameStage=" + gameStage +
                ", playerSet=" + playerSet.toString() +
                ")"
    }
}
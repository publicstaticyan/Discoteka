package discoteka.game

import discoteka.DiscoEngine.Companion.getInstance
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


object Arena {
    private const val CELL_SIZE = 3;
    private const val GRID_SIZE = 15;
    private val usedColors: MutableList<Material> = ArrayList()
    private val startLocation = Location(Bukkit.getWorld("world"), 0.0, 60.0, 0.0)
    private var step: Double = 0.2
    private val COLORS = arrayOf(
        Material.ORANGE_CONCRETE,
        Material.MAGENTA_CONCRETE,
        Material.LIGHT_BLUE_CONCRETE,
        Material.YELLOW_CONCRETE,
        Material.LIME_CONCRETE,
        Material.PINK_CONCRETE,
        Material.CYAN_CONCRETE,
        Material.PURPLE_CONCRETE,
        Material.BLUE_CONCRETE,
        Material.BROWN_CONCRETE,
        Material.GREEN_CONCRETE,
        Material.RED_CONCRETE,
    )
    
    fun spawn() {
        buildGrid { block, material ->
            block.type = material
        }
    }

    fun destroy() {
        buildGrid { block, _ ->
            block.type = Material.AIR
        }
    }

    fun getCenter(): Location {
        val centerOffset = ((GRID_SIZE * CELL_SIZE) / 2).toDouble()
        return startLocation.clone().add(centerOffset, 2.0, centerOffset)
    }

    private fun buildGrid(action: (Block, Material) -> Unit) {
        usedColors.clear()
        step = 0.2

        buildArena(action)
    }

    private fun buildArena(action: (Block, Material) -> Unit) {
        for (row in 0 until GRID_SIZE) {
            for (col in 0 until GRID_SIZE) {
                // col 1 * cell_size 5 = 5 || col 2 * cell_size 5 = 10 ... ||
                val cellStart: Location = startLocation.clone().add(
                    (col * CELL_SIZE).toDouble(), 0.0,
                    (row * CELL_SIZE).toDouble()
                )

                val material = getUniqueConcrete()

                buildCell(cellStart, material, action)

                if (usedColors.size >= COLORS.size - 1) {
                    usedColors.clear()
                }
            }
        }
    }

    private fun buildCell(cellStart: Location, material: Material, action: (Block, Material) -> Unit) {
        for (x in 0 until CELL_SIZE) {
            for (z in 0 until CELL_SIZE) {
                val block = Bukkit.getWorld("world")!!.getBlockAt(cellStart.clone().add(x.toDouble(), 0.0, z.toDouble()))

                object : BukkitRunnable() {
                    override fun run() {
                        action(block, material)
                    }
                }.runTaskLater(getInstance(), step.toLong())
                step+=0.2
            }
        }
    }

    private fun getUniqueConcrete(): Material {
        val random = Random()
        var color: Material

        do {
            color = COLORS[random.nextInt(COLORS.size)]
        } while (usedColors.contains(color))

        usedColors.add(color)

        return color;
    }
}
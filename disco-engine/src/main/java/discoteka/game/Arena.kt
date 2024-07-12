package discoteka.game

import discoteka.DiscoEngine.Companion.getInstance
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object Arena {
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

    private const val CELL_SIZE = 5;
    private const val GRID_SIZE = 30;
    private var step: Double = 0.2
    private val usedColors: MutableList<Material> = ArrayList()
    private val startLocation = Location(Bukkit.getWorld("world"), 0.0, 60.0, 0.0)


    fun spawn() {
        buildGrid { block, material ->
            block.type = material
        }
//        buildPath { block, material ->
//            block.type = material
//        }
    }

    fun destroy() {
        buildGrid { block, _ ->
            block.type = Material.AIR
        }
//        buildPath { block, _ ->
//            block.type = Material.AIR
//        }
    }

    private fun buildGrid(action: (Block, Material) -> Unit) {
        usedColors.clear()
        step = 0.2

        buildArena(action)
    }


    private fun buildArena(action: (Block, Material) -> Unit) {
        val numCells = GRID_SIZE / CELL_SIZE

        for (row in 0 until numCells) {
            for (col in 0 until numCells) {
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
                step += 0.2
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

    val center: Location
        get() {
            val centerOffset = (GRID_SIZE / 2).toDouble()
            return startLocation.clone().add(centerOffset, 2.0, centerOffset)
        }

    private fun buildPath(action: (Block, Material) -> Unit) {
        val pathMaterial = Material.SMOOTH_STONE
        val pathWidth = 3
        val gap = 3
        val arenaSize = GRID_SIZE
        val elevation = 3

        val innerEdgeStart = startLocation.clone().add(-gap.toDouble(), 0.0, -gap.toDouble())
        val innerEdgeEnd = startLocation.clone().add((arenaSize + gap).toDouble(), 0.0, (arenaSize + gap).toDouble())

        val outerEdgeStart = innerEdgeStart.clone().add(-pathWidth.toDouble(), 0.0, -pathWidth.toDouble())
        val outerEdgeEnd = innerEdgeEnd.clone().add(pathWidth.toDouble(), 0.0, pathWidth.toDouble())

        // Build the path around the arena with a gap
        for (x in outerEdgeStart.blockX until outerEdgeEnd.blockX) {
            for (z in outerEdgeStart.blockZ until outerEdgeEnd.blockZ) {

                if ((x in (outerEdgeStart.blockX until innerEdgeStart.blockX) ||
                            x in (innerEdgeEnd.blockX until outerEdgeEnd.blockX)) ||
                    (z in (outerEdgeStart.blockZ until innerEdgeStart.blockZ) ||
                            z in (innerEdgeEnd.blockZ until outerEdgeEnd.blockZ))) {
                    val blockLocation = startLocation.world?.getBlockAt(x, startLocation.blockY + elevation, z)?.location
                    blockLocation?.block?.type = pathMaterial
                }
            }
        }

        innerEdgeEnd.block.type = Material.PURPLE_CONCRETE
        innerEdgeStart.block.type = Material.MAGENTA_CONCRETE
        outerEdgeEnd.block.type = Material.GREEN_CONCRETE
        outerEdgeStart.block.type = Material.LIME_CONCRETE
    }
}
package discoteka.game

import discoteka.DiscoEngine.Companion.getInstance
import discoteka.constants.Constants
import discoteka.utils.DiscoUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object Arena {

    private var CELL_SIZE_X = 3
    private var CELL_SIZE_Z = 3
    private var GRID_SIZE = 42;
    private var DELAY = 0.0

    private val usedColors = mutableListOf<Material>()
    private val usedConcrete = mutableListOf<ItemStack>()
    private val startLocation = Location(Bukkit.getWorld("world"), 0.0, 60.0, 0.0)
    private const val step = 0.05

    fun modifyProperties(cellSizeX: Int, cellSizeZ: Int, gridSize: Int) {
        CELL_SIZE_X = cellSizeX
        CELL_SIZE_Z = cellSizeZ
        GRID_SIZE = gridSize
    }

    fun spawn(): List<ItemStack> {
        usedConcrete.clear()

        buildGrid { block, material ->
            block.type = material
        }

        return usedConcrete
    }

    fun destroy() {
        buildGrid { block, _ ->
            block.type = Material.AIR
        }
    }

    private fun buildGrid(action: (Block, Material) -> Unit) {
        usedColors.clear()
        DELAY = 0.0

        buildArena(action)
    }


    private fun buildArena(action: (Block, Material) -> Unit) {
        val NUM_CELLS_X = GRID_SIZE / CELL_SIZE_X
        val NUM_CELLS_Z = GRID_SIZE / CELL_SIZE_Z

        for (row in 0 until NUM_CELLS_Z) {
            for (col in 0 until NUM_CELLS_X) {
                val cellStart: Location = startLocation.clone().add(
                    (col * CELL_SIZE_X).toDouble(), 0.0,
                    (row * CELL_SIZE_Z).toDouble()
                )

                val concrete = DiscoUtils.getRandomConcreteColor(usedColors)

                usedConcrete.add(concrete)

                buildCell(cellStart, concrete.type, action)
            }
        }
    }

    private fun buildCell(cellStart: Location, material: Material, action: (Block, Material) -> Unit) {
        for (x in 0 until CELL_SIZE_X) {
            for (z in 0 until CELL_SIZE_Z) {
                val block = Bukkit.getWorld("world")!!.getBlockAt(cellStart.clone().add(x.toDouble(), 0.0, z.toDouble()))

//                object : BukkitRunnable() {
//                    override fun run() {
                        action(block, material)
//                    }
//                }.runTaskLater(getInstance(), DELAY.toLong())
//                DELAY += step
            }
        }
    }

    fun purgeBlocks(chosenMaterial: Material) {
        for (x in startLocation.blockX until startLocation.blockX + GRID_SIZE) {
            for (z in startLocation.blockZ until startLocation.blockZ + GRID_SIZE) {
                val block = Bukkit.getWorld("world")!!.getBlockAt(x, startLocation.blockY, z)

                if (block.type != chosenMaterial) {
                    block.type = Material.AIR
                }
            }
        }
    }

    val center: Location
        get() {
            val centerOffsetX = (GRID_SIZE / 2).toDouble()
            val centerOffsetZ = (GRID_SIZE / 2).toDouble()
            return startLocation.clone().add(centerOffsetX, 2.0, centerOffsetZ)
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
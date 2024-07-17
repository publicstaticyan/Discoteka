package discoteka.constants

import discoteka.utils.StackBuilder
import org.bukkit.Material
import java.util.Stack

class Constants {
    companion object {
        fun getRandomPair(round: Int): Pair<Int, Int> {
            return when {
                round < 5 -> EASY_GRID.random()
                round in 5..9 -> MEDIUM_GRID.random()
                else -> HARD_GRID.random()
            }
        }

        const val DISCO_COLORFUL_LOGO = "§b[§x§f§f§0§0§0§0§lD§x§f§f§5§f§0§0§lI§x§f§f§b§f§0§0§lS§x§b§f§f§f§0§0§lC§x§0§0§f§f§0§0§lO§x§0§0§4§0§b§f§lT§x§2§6§0§0§c§0§lE§x§5§d§0§0§9§6§lK§x§9§4§0§0§d§3§lA§b]"
        const val PLAYER_MIN = 2
        val VOTE_CHEST_ITEM = StackBuilder(Material.CHEST).setName("§e§lVOTAÇÃO").toItemStack()
        private val EASY_GRID = listOf(
            Pair(42, 21),
            Pair(21, 21),
            Pair(14, 14),
            Pair(42, 14),
            Pair(42, 7),
            Pair(7, 7),
            Pair(42, 6),
            Pair(6, 6),
        )
        private val MEDIUM_GRID = listOf(
            Pair(42, 3),
            Pair(14, 3),
            Pair(7, 3),
            Pair(6, 3),
            Pair(3, 3),
        )
        private val HARD_GRID = listOf(
            Pair(42, 2),
            Pair(3, 2),
            Pair(2, 2),
            Pair(2, 1),
            Pair(1, 1),
            Pair(42, 1),
        )
        val COLORS = arrayOf(
            StackBuilder(Material.ORANGE_CONCRETE).setName("§6§lLARANJA"),
            StackBuilder(Material.MAGENTA_CONCRETE).setName("§d§lMAGENTA"),
            StackBuilder(Material.LIGHT_BLUE_CONCRETE).setName("§b§lAZUL CLARO"),
            StackBuilder(Material.YELLOW_CONCRETE).setName("§e§lAMARELO"),
            StackBuilder(Material.LIME_CONCRETE).setName("§a§lVERDE CLARO"),
            StackBuilder(Material.PINK_CONCRETE).setName("§d§lROSA"),
            StackBuilder(Material.CYAN_CONCRETE).setName("§3§lCIANO"),
            StackBuilder(Material.PURPLE_CONCRETE).setName("§5§lROXO"),
            StackBuilder(Material.BLUE_CONCRETE).setName("§9§lAZUL"),
            StackBuilder(Material.GREEN_CONCRETE).setName("§2§lVERDE"),
            StackBuilder(Material.RED_CONCRETE).setName("§4§lVERMELHO"),
        )
    }
}
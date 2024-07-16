package discoteka.enums

import discoteka.utils.StackBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class Music(
    val title: String,
    val author: String,
    val url: String,
    val item: ItemStack
) {

    TAKE_ON_ME(
        "Take on Me",
        "Aha",
        "https://www.youtube.com/watch?v=djV11Xbc914",
        StackBuilder(Material.MUSIC_DISC_CAT).setName("§aTake on Me").toItemStack()),
    FIRST_OF_THE_YEAR(
        "First of the Year",
        "Skrillex",
        "https://www.youtube.com/watch?v=TYYyMu3pzL4",
        StackBuilder(Material.MUSIC_DISC_CHIRP).setName("§aFirst of the Year").toItemStack()),
    BALLS_IN_YOUR_JAWS(
        "Balls in Your Jaws",
        "40 Cent",
        "https://www.youtube.com/watch?v=G4KkJ0Q8VgI",
        StackBuilder(Material.MUSIC_DISC_MALL).setName("§aBalls in your jaws").toItemStack()
    )

}
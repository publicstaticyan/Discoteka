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
        StackBuilder(Material.MUSIC_DISC_CHIRP).setName("§bFirst of the Year").toItemStack()),
    BAD_ROMANCE(
        "Bad Romance",
        "Lady Gaga",
        "https://www.youtube.com/watch?v=qrO4YZeyl0I",
        StackBuilder(Material.MUSIC_DISC_CAT).setName("§dBad Romance").toItemStack()),
    PARTY_ROCK_ANTHEM(
        "Party Rock Anthem",
        "LMFAO",
        "https://youtu.be/KQ6zr6kCPj8?si=GjMkhe7FFdpNOOOa&t=85",
        StackBuilder(Material.MUSIC_DISC_11).setName("§eParty Rock Anthem").toItemStack()),
    HAPPY(
        "Happy",
        "Pharrell Williams",
        "https://www.youtube.com/watch?v=ZbZSe6N_BXs",
        StackBuilder(Material.MUSIC_DISC_13).setName("§9Happy").toItemStack()),
    ALL_STAR(
        "All Star",
        "Smash Mouth",
        "https://www.youtube.com/watch?v=L_jWHffIx5E",
        StackBuilder(Material.MUSIC_DISC_5).setName("§6All Star").toItemStack()),
    SEE_TIHN(
        "See Tihn",
        "Hoang Thuy Linh",
        "https://www.youtube.com/watch?v=AKChFg7ku2A",
        StackBuilder(Material.MUSIC_DISC_BLOCKS).setName("§dSee Tihn").toItemStack()),
    ATTACK_ON_TITAN_OP_1(
        "Attack on Titan - Opening 1",
        "Linked Horizon",
        "https://www.youtube.com/watch?v=8OkpRK2_gVs",
        StackBuilder(Material.MUSIC_DISC_CREATOR).setName("§bAttack on Titan Opening 1").toItemStack()),
    CALL_ME_MAYBE(
        "Call Me Maybe",
        "Carly Rae Jepsen",
        "https://www.youtube.com/watch?v=fWNaR-rxAic",
        StackBuilder(Material.MUSIC_DISC_MELLOHI).setName("§dCall me Maybe").toItemStack()),
    CARAMELLDANSEN(
        "Caramelldansen",
        "Lady Gaga",
        "https://www.youtube.com/watch?v=qrO4YZeyl0I",
        StackBuilder(Material.MUSIC_DISC_CAT).setName("§aBalls in your jaws").toItemStack()),
    GANGNAM_STYLE(
        "Gangnam Style",
        "Psy",
        "https://www.youtube.com/watch?v=9bZkp7q19f0",
        StackBuilder(Material.MUSIC_DISC_FAR).setName("§6Gangnam Style").toItemStack()),
    CIGARO(
        "Cigaro",
        "System of a Down",
        "https://www.youtube.com/watch?v=L4M98z22pgI",
        StackBuilder(Material.MUSIC_DISC_OTHERSIDE).setName("§bCigaro").toItemStack()),
    MC_POZE_ANOS_80(
        "MC Poze Anos 80",
        "PMM",
        "https://www.youtube.com/watch?v=WZIGwN-5Ioo",
        StackBuilder(Material.MUSIC_DISC_STAL).setName("§eMC Poze Anos 80").toItemStack()),
    TO_SEM_SINAL_DA_TIM(
        "To Sem Sinal da TIM",
        "Galo Frito",
        "https://www.youtube.com/watch?v=iWLE8pVLgNE",
        StackBuilder(Material.MUSIC_DISC_STRAD).setName("§aTo sem sinal da TIM").toItemStack()),
}
package discoteka.game

import discoteka.enums.Music
import org.bukkit.entity.Player

object Vote {

    private val votes = mutableMapOf<Player, Music>()

    fun vote(player: Player, id: Int) {
        val music = Music.values()[id]
        votes[player] = music
        player.sendMessage("§eVocê votou na música: §b${music.title}§e!")
    }

    fun getMostVotedMusic(): Music? {
        return votes.values.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
    }

    fun getVote(player: Player): Music? {
        return votes[player]
    }

    fun reset() {
        votes.clear()
    }
}
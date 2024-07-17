package discoteka.utils

import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.entity.Firework
import org.bukkit.inventory.ItemStack

class FireworkBuilder {
    private var `is`: Firework
    constructor(`is`: Location) {
        this.`is` = `is`.world?.spawn(`is`, Firework::class.java)!!
    }

    fun power(height: Int): FireworkBuilder {
        val fm = `is`.fireworkMeta
        fm.power = height
        `is`.fireworkMeta = fm
        return this
    }

    fun effects(fireworkEffect: FireworkEffect): FireworkBuilder {
        val fm = `is`.fireworkMeta
        fm.addEffects(fireworkEffect)
        `is`.fireworkMeta = fm
        return this
    }

}
package discoteka.utils

import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import java.util.*

/**
 * Easily create itemstacks, without messing your hands.
 * *Note that if you do use this in one of your projects, leave this notice.*
 * *Please do credit me if you do use this in one of your projects.*
 * @author NonameSL
 */
class StackBuilder {
    private var `is`: ItemStack

    /**
     * Create a new ItemBuilder over an existing itemstack.
     * @param is The itemstack to create the ItemBuilder over.
     */
    constructor(`is`: ItemStack) {
        this.`is` = `is`
    }
    /**
     * Create a new ItemBuilder from scratch.
     * @param m The material of the item.
     * @param amount The amount of the item.
     */
    /**
     * Create a new ItemBuilder from scratch.
     * @param m The material to create the ItemBuilder with.
     */
    @JvmOverloads
    constructor(m: Material?, amount: Int = 1) {
        `is` = ItemStack(m!!, amount)
    }

    /**
     * Create a new ItemBuilder from scratch.
     * @param m The material of the item.
     * @param amount The amount of the item.
     * @param durability The durability of the item.
     */
    constructor(m: Material?, amount: Int, durability: Byte) {
        `is` = ItemStack(m!!, amount, durability.toShort())
    }

    /**
     * Clone the ItemBuilder into a new one.
     * @return The cloned instance.
     */
    fun clone(): StackBuilder {
        return StackBuilder(`is`)
    }

    /**
     * Change the durability of the item.
     * @param dur The durability to set it to.
     */
    fun setDurability(dur: Short): StackBuilder {
        `is`.durability = dur
        return this
    }

    /**
     * Set the displayname of the item.
     * @param name The name to change it to.
     */
    fun setName(name: String?): StackBuilder {
        val im = `is`.itemMeta
        im!!.setDisplayName(name)
        `is`.setItemMeta(im)
        return this
    }

    fun setPotionEffect(
        potionEffectType: PotionEffectType?,
        potionType: PotionType,
        time: Int,
        level: Int,
        overrideEffects: Boolean
    ): StackBuilder {
        val potionMeta = `is`.itemMeta as PotionMeta?
        potionMeta!!.addCustomEffect(PotionEffect(potionEffectType!!, time, level), overrideEffects)
        potionMeta.color = potionType.effectType!!.color
        `is`.setItemMeta(potionMeta)
        return this
    }

    /**
     * Add an unsafe enchantment.
     * @param ench The enchantment to add.
     * @param level The level to put the enchant on.
     */
    fun addUnsafeEnchantment(ench: Enchantment?, level: Int): StackBuilder {
        `is`.addUnsafeEnchantment(ench!!, level)
        return this
    }

    /**
     * Remove a certain enchant from the item.
     * @param ench The enchantment to remove
     */
    fun removeEnchantment(ench: Enchantment?): StackBuilder {
        `is`.removeEnchantment(ench!!)
        return this
    }

    /**
     * Set the skull owner for the item. Works on skulls only.
     * @param owner The name of the skull's owner.
     */
    fun setSkullOwner(owner: String?): StackBuilder {
        try {
            val im = `is`.itemMeta as SkullMeta?
            im!!.setOwner(owner)
            `is`.setItemMeta(im)
        } catch (expected: ClassCastException) {
            expected.printStackTrace()
        }
        return this
    }

    /**
     * Add an enchant to the item.
     * @param ench The enchant to add
     * @param level The level
     */
    fun addEnchant(ench: Enchantment?, level: Int): StackBuilder {
        val im = `is`.itemMeta
        im!!.addEnchant(ench!!, level, true)
        `is`.setItemMeta(im)
        return this
    }

    fun addFlag(itemFlag: ItemFlag): StackBuilder {
        val im = `is`.itemMeta
        im!!.addItemFlags(itemFlag)
        `is`.setItemMeta(im)
        return this
    }

    /**
     * Add multiple enchants at once.
     * @param enchantments The enchants to add.
     */
    fun addEnchantments(enchantments: Map<Enchantment?, Int?>?): StackBuilder {
        `is`.addEnchantments(enchantments!!)
        return this
    }

    /**
     * Sets infinity durability on the item by setting the durability to Short.MAX_VALUE.
     */
    fun setInfinityDurability(): StackBuilder {
        `is`.durability = Short.MAX_VALUE
        return this
    }

    /**
     * Re-sets the lore.
     * @param lore The lore to set it to.
     */
    fun setLore(vararg lore: String?): StackBuilder {
        val im = `is`.itemMeta
        im!!.lore = Arrays.asList(*lore)
        `is`.setItemMeta(im)
        return this
    }

    /**
     * Re-sets the lore.
     * @param lore The lore to set it to.
     */
    fun setLore(lore: List<String?>?): StackBuilder {
        val im = `is`.itemMeta
        im!!.lore = lore
        `is`.setItemMeta(im)
        return this
    }

    /**
     * Remove a lore line.
     * @param line The lore to remove.
     */
    fun removeLoreLine(line: String): StackBuilder {
        val im = `is`.itemMeta
        val lore: MutableList<String> = ArrayList(im!!.lore)
        if (!lore.contains(line)) return this
        lore.remove(line)
        im.lore = lore
        `is`.setItemMeta(im)
        return this
    }

    /**
     * Remove a lore line.
     * @param index The index of the lore line to remove.
     */
    open fun removeLoreLine(index: Int): StackBuilder? {
        val im = `is`.itemMeta
        val lore = ArrayList(im!!.lore)
        if (index < 0 || index > lore.size) return this
        lore[index] = null
        im.lore = lore
        `is`.setItemMeta(im)
        return this
    }

    /**
     * Add a lore line.
     * @param line The lore line to add.
     */
    fun addLoreLine(line: String?): StackBuilder {
        val im = `is`.itemMeta
        var lore: MutableList<String?> = ArrayList()
        if (im!!.hasLore()) lore = ArrayList(im.lore)
        lore.add(line)
        im.lore = lore
        `is`.setItemMeta(im)
        return this
    }

    /**
     * Add a lore line.
     * @param line The lore line to add.
     * @param pos The index of where to put it.
     */
    fun addLoreLine(line: String, pos: Int): StackBuilder {
        val im = `is`.itemMeta
        val lore: MutableList<String> = ArrayList(im!!.lore)
        lore[pos] = line
        im.lore = lore
        `is`.setItemMeta(im)
        return this
    }

    /**
     * Sets the dye color on an item.
     * *** Notice that this doesn't check for item type, sets the literal data of the dyecolor as durability.**
     * @param color The color to put.
     */
    fun setDyeColor(color: DyeColor): StackBuilder {
        `is`.durability = color.dyeData.toShort()
        return this
    }

    /**
     * Sets the dye color of a wool item. Works only on wool.
     * @see StackBuilder@setDyeColor
     * @param color The DyeColor to set the wool item to.
     */
    @Deprecated(
        """As of version 1.2 changed to setDyeColor.
      """
    )
    fun setWoolColor(color: DyeColor): StackBuilder {
        if (`is`.type != Material.LEGACY_WOOL) return this
        `is`.durability = color.woolData.toShort()
        return this
    }

    /**
     * Sets the armor color of a leather armor piece. Works only on leather armor pieces.
     * @param color The color to set it to.
     */
    fun setLeatherArmorColor(color: Color?): StackBuilder {
        try {
            val im = `is`.itemMeta as LeatherArmorMeta?
            im!!.setColor(color)
            `is`.setItemMeta(im)
        } catch (expected: ClassCastException) {
            expected.printStackTrace()
        }
        return this
    }

    /**
     * Retrieves the itemstack from the ItemBuilder.
     * @return The itemstack created/modified by the ItemBuilder instance.
     */
    fun toItemStack(): ItemStack {
        return `is`
    }
}

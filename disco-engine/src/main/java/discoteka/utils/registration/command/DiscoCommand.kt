package discoteka.utils.registration.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class DiscoCommand(val commandName: String, private val permission: Boolean) : CommandExecutor {
    var description = ""
    var aliases: Array<String?> = arrayOfNulls(0)

    abstract fun onCommand(sender: CommandSender?, args: Array<String>?)
    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (permission && !sender.hasPermission("disco.$commandName")) {
                sender.sendMessage("§cError: You don't have permission to use this command")
                return true
            }
        }
        onCommand(sender, args)
        return false
    }
}
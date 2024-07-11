package discoteka.utils.registration.command

import discoteka.DiscoEngine
import discoteka.utils.ClassGetter.getClassesForPackage
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.plugin.Plugin
import java.lang.reflect.Constructor
import java.util.*

object CommandLoader {
    private var commandMap: SimpleCommandMap? = null
    fun registerCommands() {
        var i = 0
        val srcPackage = CommandLoader::class.java.getPackage().name.substring(
            0,
            CommandLoader::class.java.getPackage().name.indexOf('.')
        )
        try {
            val commandmapfield = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
            commandmapfield.isAccessible = true
            commandMap = commandmapfield[Bukkit.getServer()] as SimpleCommandMap
        } catch (e: Exception) {
            Bukkit.getLogger().warning("[CommandLoader] Error when trying to access the Command Map")
        }
        for (commandClass in getClassesForPackage(DiscoEngine.getInstance(), srcPackage)) {
            if (DiscoCommand::class.java.isAssignableFrom(commandClass) && commandClass != DiscoCommand::class.java) {
                try {
                    val command: DiscoCommand = commandClass.getConstructor().newInstance() as DiscoCommand
                    registerCommand(
                        command as CommandExecutor,
                        command.commandName,
                        command.description,
                        command.aliases
                    )
                    i++
                } catch (e: Exception) {
                    e.printStackTrace()
                    DiscoEngine.getInstance().logger
                        .warning("[CommandLoader] Error when registering the command " + commandClass.name)
                }
            }
        }
        DiscoEngine.getInstance().logger.info("[CommandLoader] $i commands loaded")
    }

    private fun registerCommand(
        executor: CommandExecutor,
        name: String,
        description: String,
        aliases: Array<String?>
    ): PluginCommand? {
        return try {
            var command: PluginCommand? = DiscoEngine.getInstance().getCommand(name.lowercase(Locale.getDefault()))
            if (command == null) {
                val constructor: Constructor<*> =
                    PluginCommand::class.java.getDeclaredConstructor(String::class.java, Plugin::class.java)
                constructor.isAccessible = true
                command = constructor.newInstance(*arrayOf(name, DiscoEngine.getInstance())) as PluginCommand
            }
            command.setExecutor(executor)
            command.setAliases(listOf(*aliases))
            command.setDescription(description)
            commandMap!!.register(name, (command as Command))
            command
        } catch (e: Exception) {
            DiscoEngine.getInstance().logger.warning("[CommandLoader] Error when registering the command $name")
            null
        }
    }
}
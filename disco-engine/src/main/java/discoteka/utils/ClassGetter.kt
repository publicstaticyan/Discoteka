package discoteka.utils

import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.net.URL
import java.util.jar.JarFile

object ClassGetter {
    fun getClassesForPackage(plugin: JavaPlugin, pkgname: String): ArrayList<Class<*>> {
        val classes = ArrayList<Class<*>>()
        val src = plugin.javaClass.protectionDomain.codeSource
        if (src != null) {
            val resource = src.location
            resource.path
            processJarfile(resource, pkgname, classes)
        }
        return classes
    }

    private fun loadClass(className: String): Class<*>? {
        return try {
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException("Unexpected ClassNotFoundException loading class '$className'")
        } catch (e: NoClassDefFoundError) {
            null
        }
    }

    private fun processJarfile(resource: URL, pkgname: String, classes: ArrayList<Class<*>>) {
        val jarFile: JarFile
        val relPath = pkgname.replace('.', '/')
        val resPath = resource.path.replace("%20", " ")
        val jarPath = resPath.replaceFirst("[.]jar[!].*".toRegex(), ".jar").replaceFirst("file:".toRegex(), "")
        jarFile = try {
            JarFile(jarPath)
        } catch (e: IOException) {
            throw RuntimeException("Unexpected IOException reading JAR File '$jarPath'", e)
        }
        val entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            val entryName = entry.name
            var className: String? = null
            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length > relPath.length + "/".length) className =
                entryName.replace('/', '.').replace('\\', '.').replace(".class", "")
            if (className != null) {
                val c = loadClass(className)
                if (c != null) classes.add(c)
            }
        }
        try {
            jarFile.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
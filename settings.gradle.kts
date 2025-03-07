rootProject.name = "command-manager"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev")
        maven("https://maven.parchmentmc.org")
        maven("https://files.minecraftforge.net/maven")
        mavenCentral()
    }
}

fun defineProject(module: String, path: String) {
    include(module)
    project(module).projectDir = file(path)
}

defineProject(":command-manager-common", "src/common")
//defineProject(":command-manager-fabric-20", "src/fabric-20") // Only for archival purposes
defineProject(":command-manager-fabric-21", "src/fabric-21")
defineProject(":command-manager-velocity", "src/velocity")
// TODO: Spigot (Bukkit) Support
// TODO: Force Support

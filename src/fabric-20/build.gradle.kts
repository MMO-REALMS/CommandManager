@file:Suppress("UnstableApiUsage")

plugins {
    id(libs.plugins.loom.v20.get().pluginId) version libs.plugins.loom.v20.get().version.toString()
}

dependencies {
    // Minecraft
    minecraft(libs.minecraft.v20)
    mappings(loom.layered {
        officialMojangMappings()
        parchment(libs.parchment.mappings.v20)
    })

    modCompileOnly(libs.fabric.loader.v20)
    modCompileOnly(libs.fabric.api.v20)

    // Project
    api(project(":command-manager-common"))

    // Dependencies
    if (project.properties["com.voinearadu.utils.local"] != null) {
        api(project(project.properties["com.voinearadu.utils.local"] as String))
    } else {
        api(libs.voinearadu.utils)
    }
    api(libs.luckperms)
    modApi(libs.kyori.adventure.fabric)

    // Annotations
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    compileOnly(libs.jetbrains.annotations)
    annotationProcessor(libs.jetbrains.annotations)
    testCompileOnly(libs.jetbrains.annotations)
    testAnnotationProcessor(libs.jetbrains.annotations)
}

tasks {
    jar {
        archiveFileName = "FabricCommandManager-$version.jar"
    }

    processResources {
        inputs.property("version", version)

        filesMatching("fabric.mod.json") {
            expand("version" to version)
        }
    }

    remapJar {
        archiveFileName = "FabricCommandManager-$version.jar"
    }
}
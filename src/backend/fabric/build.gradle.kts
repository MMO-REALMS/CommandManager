@file:Suppress("UnstableApiUsage")

import Utils.Companion.toPascalCase
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.named

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("architectury-plugin")
    id("dev.architectury.loom")
    id("com.gradleup.shadow")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

val (type, module, id) = Utils.getProjectMetadata(project.name)

loom {
    silentMojangMappingsLicense()
}

dependencies {
    minecraft(Libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment(Libs.parchment)
    })

    modCompileOnly(Libs.fabric.loader)
    modCompileOnly(Libs.fabric.api)
    modCompileOnly(Libs.architectury.fabric)

    api(project(":command-manager-backend-common"))
    implementation(project(":command-manager-backend-common", configuration = "transformProductionFabric"))

    modCompileOnly("net.kyori:adventure-platform-fabric:6.4.0")
}

tasks {
    shadowJar {
        archiveFileName = "CommandManager-Fabric-${version}-all.jar"

        configurations = listOf(
            project.configurations.api.get(),
            project.configurations.implementation.get(),
            project.configurations.modApi.get(),
            project.configurations.modImplementation.get()
        )
        exclude("architectury.common.json")
    }

    remapJar {
        archiveFileName = "CommandManager-Fabric-${version}.jar"
        injectAccessWidener.set(true)
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }

    jar {
        archiveClassifier.set("dev")
    }
}

components.getByName("java") {
    this as AdhocComponentWithVariants
    this.withVariantsFromConfiguration(project.configurations["shadowRuntimeElements"]) {
        skip()
    }
}

configurations{
    implementation{
        isCanBeResolved = true
    }
    api{
        isCanBeResolved=true
    }
}
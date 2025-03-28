plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

var _version = libs.versions.version.get()
var _group = libs.versions.group.get()

version = _version
group = _group

fun DependencyHandlerScope.applyDependencies() {
    // Dependencies
    if (project.properties["com.raduvoinea.utils.local"] != null) {
        api(project(project.properties["com.raduvoinea.utils.local"] as String))
    } else {
        api(libs.raduvoinea.utils)
    }

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

fun RepositoryHandler.applyRepositories() {
    mavenCentral()
    maven("https://maven.parchmentmc.org/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.raduvoinea.com/repository/maven-releases/")
}

repositories {
    applyRepositories()
}

dependencies {
    applyDependencies()
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = _group
    version = _version

    repositories {
        applyRepositories()
    }

    dependencies {
        applyDependencies()
    }

    tasks {
        java {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }

        repositories {
            if (project.properties["com.raduvoinea.publish"] == "true") {
                maven(url = (project.findProperty("com.raduvoinea.url") ?: "") as String) {
                    name = "raduvoineaRepository"
                    credentials(PasswordCredentials::class) {
                        username = (project.findProperty("com.raduvoinea.auth.username") ?: "") as String
                        password = (project.findProperty("com.raduvoinea.auth.password") ?: "") as String
                    }
                }
            }
        }
    }
}

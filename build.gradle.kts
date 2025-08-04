plugins {
    id("standard-conventions")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "9.0.0-rc2"
}

dependencies {
    implementation(project(":core"))
}

val groupString = group.toString()

tasks {
    runServer {
        minecraftVersion("1.21.4")

        downloadPlugins {
            modrinth("bettermodel", "1.10.3")
        }
    }

    jar {
        finalizedBy(shadowJar)
    }

    shadowJar {
        archiveClassifier = ""
        dependencies {
            exclude(dependency("org.jetbrains:annotations:26.0.2"))
            exclude(dependency("org.jetbrains:annotations:13.0"))
        }
        fun prefix(pattern: String) {
            relocate(pattern, "$groupString.shaded.$pattern")
        }
        prefix("kotlin")
        prefix("dev.jorel.commandapi")
    }
}

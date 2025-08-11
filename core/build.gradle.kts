import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml

plugins {
    id("standard-conventions")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18" apply false
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.0"
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:10.1.2")
    implementation(project(":api"))
    implementation(project(":nms"))
}

paperPluginYaml {
    name = rootProject.name
    main = "$group.BetterModelForCutscenePluginImpl"
    version = project.version.toString()
    authors.add("Bindglam")
    apiVersion = "1.21"
    dependencies {
        server("BetterModel", PaperPluginYaml.Load.BEFORE, true, true)
    }
}
plugins {
    id("net.neoforged.moddev") version "2.0.39-beta"
    id("com.almostreliable.almostgradle") version "1.1.+"
}

repositories {
    maven("https://maven.latvian.dev/releases")
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
    maven("https://maven.createmod.net")
    maven("https://maven.tterrag.com")
    maven("https://jitpack.io")
    maven("https://www.cursemaven.com")
}

almostgradle.setup {
    testMod = true
}

dependencies {
    val kubejsVersion: String by project
    implementation("dev.latvian.mods:kubejs-neoforge:${kubejsVersion}")
    testImplementation("dev.latvian.mods:kubejs-neoforge:${kubejsVersion}")

    val flywheelVersion: String by project
    implementation("dev.engine-room.flywheel:flywheel-neoforge-${almostgradle.minecraftVersion}:${flywheelVersion}")
    jarJar("dev.engine-room.flywheel:flywheel-neoforge-${almostgradle.minecraftVersion}:${flywheelVersion}") {
        version {
            strictly("[1.0,2.0)")
            prefer(flywheelVersion)
        }
    }

    val ponderVersion: String by project
    implementation("net.createmod.ponder:Ponder-NeoForge-${almostgradle.minecraftVersion}:${ponderVersion}")
    jarJar("net.createmod.ponder:Ponder-NeoForge-${almostgradle.minecraftVersion}:${ponderVersion}") {
        version {
            strictly("[1.0,2.0)")
            prefer(ponderVersion)
        }
    }

    testLocalRuntime(almostgradle.recipeViewers.emi.dependency)
}

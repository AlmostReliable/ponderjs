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
    jarJar("dev.engine-room.flywheel:flywheel-neoforge-${almostgradle.minecraftVersion}:${flywheelVersion}")
    implementation("dev.engine-room.flywheel:flywheel-neoforge-${almostgradle.minecraftVersion}:${flywheelVersion}")

    val ponderVersion: String by project
    jarJar("net.createmod.ponder:Ponder-NeoForge-${almostgradle.minecraftVersion}:${ponderVersion}")
    implementation("net.createmod.ponder:Ponder-NeoForge-${almostgradle.minecraftVersion}:${ponderVersion}")

    testLocalRuntime(almostgradle.recipeViewers.emi.dependency)
}

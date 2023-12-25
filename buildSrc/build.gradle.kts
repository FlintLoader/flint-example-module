import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.8.21"
}

repositories {
    mavenCentral()
    maven("https://mcentral.firstdark.dev/releases")
    gradlePluginPortal()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile> {
    val targetVersion = 8
    if (JavaVersion.current().isJava9Compatible) {
        options.release.set(targetVersion)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation("xyz.wagyourtail.unimined:unimined:1.1.0-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.9.0")
}
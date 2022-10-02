import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application
}

group = "com.github.mschirmacher"
version = "1.0-SNAPSHOT"

val kotestVersion: String by project
val mordantVersion: String by project

repositories {
    mavenCentral()
}

dependencies {

    implementation("com.github.ajalt.mordant:mordant:$mordantVersion")

    testImplementation(kotlin("test"))
    testImplementation(project.dependencies.platform("io.kotest:kotest-bom:$kotestVersion"))
    testImplementation("io.kotest:kotest-runner-junit5") {
        exclude("io.mockk")
    }
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-framework-datatest")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("com.github.mschirmacher.infrastructure.ApplicationKt")
}
tasks.withType<JavaExec>() {
    standardInput = System.`in`
}

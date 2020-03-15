
plugins {
    kotlin("jvm") version "1.3.70"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.microutils:kotlin-logging:1.7.8")
    implementation("org.slf4j:slf4j-jdk14:1.7.25")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("org.assertj:assertj-core:3.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    // Use the built-in JUnit support of Gradle.
    "test"(Test::class) {
        useJUnitPlatform()
    }

}
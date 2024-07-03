import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("org.jetbrains.kotlinx.kover") version "0.7.6"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

group = "austral.ingsis"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/tomasfreile/printscript")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/austral-ingsis/class-redis-stream")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter")

    implementation("PrintScript:lexer:1.1.19")
    implementation("PrintScript:commons:1.1.19")
    implementation("PrintScript:interpreter:1.1.19")
    implementation("PrintScript:parser:1.1.19")
    implementation("PrintScript:cli:1.1.19")
    implementation("PrintScript:formatter:1.1.19")
    implementation("PrintScript:sca:1.1.19")

    implementation("org.python:jython-standalone:2.7.2")

    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.austral.ingsis:redis-streams-flux:0.1.13")

    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named("check") {
    dependsOn("ktlintCheck")
    dependsOn("koverVerify")
}

koverReport {
    verify {
        rule {
            minBound(80)
        }
    }
}

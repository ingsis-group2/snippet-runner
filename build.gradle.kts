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

group = "austral.ingsis"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/tomasfreile/printscript")
        credentials {
            username = (project.findProperty("gpr.user") ?: System.getenv("USERNAME")).toString()
            password = (project.findProperty("gpr.key") ?: System.getenv("TOKEN")).toString()
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("PrintScript:lexer:1.1.12")
    implementation("PrintScript:commons:1.1.12")
    implementation("PrintScript:interpreter:1.1.12")
    implementation("PrintScript:parser:1.1.12")
    implementation("PrintScript:cli:1.1.12")
    implementation("PrintScript:formatter:1.1.12")
    implementation("PrintScript:sca:1.1.12")

    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.junit.jupiter:junit-jupiter")
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
            minBound(50)
        }
    }
}

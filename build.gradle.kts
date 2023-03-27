import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

plugins {
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    id("org.liquibase.gradle") version "2.2.0"

    kotlin("jvm") version "1.7.22"
    kotlin("kapt") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.7.22"
}

group = "ru.illine"
version = "0.0.10"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("org.springframework:spring-web")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.0.7")
    implementation("com.theokanning.openai-gpt3-java:service:0.11.1")
    implementation("org.zalando:logbook-spring-boot-starter:2.16.0")
    implementation("org.zalando:logbook-okhttp:2.16.0")
    implementation("org.liquibase:liquibase-core:4.20.0")

    liquibaseRuntime("org.liquibase:liquibase-core:4.20.0")
    liquibaseRuntime("org.liquibase:liquibase-groovy-dsl:3.0.2")
    liquibaseRuntime("ch.qos.logback:logback-classic:1.4.5")
    liquibaseRuntime("ch.qos.logback:logback-classic:1.4.5")
    liquibaseRuntime("org.postgresql:postgresql:42.5.4")
    liquibaseRuntime("org.yaml:snakeyaml:2.0")
    liquibaseRuntime("info.picocli:picocli:4.6.1")
    liquibaseRuntime("javax.xml.bind:jaxb-api:2.3.1")

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    runtimeOnly("org.postgresql:postgresql:42.5.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

liquibase {
    val propertiesPath = System.getenv("LIQUIBASE_PROPERTIES_PATH") ?: "./.liquibase/liquibase.properties"
    val file = File(propertiesPath)
    val properties = Properties()
    if (file.exists()) {
        properties.load(FileInputStream(file))
    }

    val resourceDir = "./src/main/resources"
    activities.register("main") {
        this.arguments = mapOf(
            "changeLogFile" to properties.getOrDefault("changeLogFile", "$resourceDir/liquibase/changelog.yaml"),
            "url" to properties.getOrDefault("url", "jdbc:postgresql://localhost:5432/openai_telegram_bot"),
            "username" to properties.getOrDefault("username", "liquibase"),
            "password" to properties.getOrDefault("password", "liquibase"),
            "defaultSchemaName" to properties.getOrDefault("schema", "openai_telegram_bot"),
            "contexts" to properties.getOrDefault("context", "local"),
            "logLevel" to properties.getOrDefault("logLevel", "info")
        )
    }
    runList = "main"
}

tasks.jar {
    enabled = false
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    this.archiveFileName.set("openai-telegram-bot.jar")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val generatedVersionDir = "$buildDir/generated-version"

sourceSets {
    main {
        kotlin {
            output.dir(generatedVersionDir)
        }
    }
}

tasks.register("generateVersionProperties") {
    doLast {
        val propertiesFile = file("$generatedVersionDir/version.properties")
        propertiesFile.parentFile.mkdirs()
        val properties = Properties()
        properties.setProperty("version", "$version")
        val out = FileOutputStream(propertiesFile)
        properties.store(out, null)
    }
}

tasks.named("processResources") {
    dependsOn("generateVersionProperties")
}

ktlint {
    reporters {
        reporter(ReporterType.JSON)
    }
}

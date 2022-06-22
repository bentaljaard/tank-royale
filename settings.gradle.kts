val version = "0.13.4"

// Schema Generator
include("schema:jvm")
//include("schema:dotnet")

// Booter
include("booter")

// Server
include("server")

// GUI app
include("gui-app")

// Bot API
include("bot-api:java")
//include("bot-api:dotnet")

// Sample Bots archives
include("sample-bots:java")
include("sample-bots:csharp")

// Docs
include("buildDocs")

val junitVersion = "5.9.0-M1"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("tankroyale", version)
            version("node", "15.5.1")

            library("gson", "com.google.code.gson:gson:2.9.0")
            library("gson-extras", "org.danilopianini:gson-extras:0.6.0")
            library("jansi", "org.fusesource.jansi:jansi:2.4.0")
            library("java-websocket", "org.java-websocket:Java-WebSocket:1.5.3")
            library("jsonschema2pojo", "org.jsonschema2pojo:jsonschema2pojo-gradle-plugin:1.1.2")
            library("picocli", "info.picocli:picocli:4.6.3")
            library("proguard-gradle", "com.guardsquare:proguard-gradle:7.2.1")
            library("miglayout-swing", "com.miglayout:miglayout-swing:11.0")
            library("nv-i18n", "com.neovisionaries:nv-i18n:1.29")
            library("serialization-json", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
            library("slf4j-simple", "org.slf4j:slf4j-simple:2.0.0-alpha7")

            // Java testing
            library("junit-api", "org.junit.jupiter:junit-jupiter-api:$junitVersion")
            library("junit-engine", "org.junit.jupiter:junit-jupiter-engine:$junitVersion")
            library("junit-params", "org.junit.jupiter:junit-jupiter-params:$junitVersion")
            library("assertj", "org.assertj:assertj-core:3.22.0")
            library("system-stubs", "uk.org.webcompere:system-stubs-jupiter:2.0.1")

            // Kotlin testing
            library("kotest-junit5", "io.kotest:kotest-runner-junit5-jvm:5.3.0")
            library("mockk", "io.mockk:mockk:1.12.4")

            plugin("shadow-jar","com.github.johnrengelman.shadow").version("7.1.2")
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").version("1.6.20")
            plugin("kotlin-plugin-serialization", "org.jetbrains.kotlin.plugin.serialization").version("1.6.20")
            plugin("benmanes-versioning", "com.github.ben-manes.versions").version("0.42.0")
            plugin("hidetake-ssh", "org.hidetake.ssh").version("2.10.1")
            plugin("itiviti-dotnet", "com.itiviti.dotnet").version("1.9.2")
            plugin("node-gradle", "com.github.node-gradle.node").version("3.2.1")
        }
    }
}
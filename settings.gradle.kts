plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "lae-2024-i41d"
include("lesson09-reflect")
include("lesson09-TPC")
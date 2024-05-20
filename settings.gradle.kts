plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "lae-2024-i41d"
include("lesson09-reflect")
include("lesson09-TPC")
include("lesson10-logger")
include("lesson11-naivemapper")
include("lesson12-naivemapper-annotations")
include("lesson15-naivemapper-recursive-and-generics")
include("lesson18-exercises")
include("lesson20-cojen-maker")
include("lesson21-naive-mapper-dynamic")
include("lesson26-sequences")
include("lesson28-sequences-and-generators")
include("lesson29-benchmarking")
include("lesson32-suspend-functions")

plugins {
    kotlin("multiplatform") version "2.3.21"
    id("org.jetbrains.compose") version "1.12.2"
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.21"
}

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "demo.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.animation)
                implementation(compose.components.resources)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
            }
        }

        val wasmJsMain by getting {
            dependencies {
                // HomeScreen 用到 material icons（Crop/Download/Edit/ExpandLess/...）
                implementation(compose.materialIconsExtended)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.pixelcolorpicker.resources"
    generateResClass = always
}
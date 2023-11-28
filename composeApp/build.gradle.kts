import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.ExperimentalComposeLibrary






plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotest)
    kotlin("plugin.serialization") version "1.9.0"

}


tasks.register("compileJava") {

    dependsOn("compileDebugJavaWithJavac")

}

tasks.register("testClasses") {
    dependsOn("desktopTestClasses")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    jvm("desktop")

    
    sourceSets {
        val desktopMain by getting

        
        androidMain.dependencies {
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation("com.github.yausername.youtubedl-android:library:-SNAPSHOT")
            implementation("com.github.yausername.youtubedl-android:ffmpeg:-SNAPSHOT") // Optional
            implementation("com.github.yausername.youtubedl-android:aria2c:-SNAPSHOT") // Optional
            //implementation("androidx.startup:startup-runtime:${project.property("androidx-startup-version")}")
            implementation("io.insert-koin:koin-android:${project.property("koin-version")}")
            implementation("io.insert-koin:koin-androidx-compose:${project.property("koin-version")}")

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation("com.github.goxr3plus:java-stream-player:${project.property("java-stream-player-version")}")
            implementation("com.github.sapher:youtubedl-java:${project.property("sapher-youtubedl-commit")}")
        }
        commonMain.dependencies {

            val voyagerVersion= project.property("voyager-version")

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation("dev.kotbase:couchbase-lite:${project.property("kotbase-version")}")
            implementation("io.insert-koin:koin-core:${project.property("koin-version")}")
            implementation("io.insert-koin:koin-test:${project.property("koin-version")}")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${project.property("kotlinx-serialization-version")}")
            implementation("com.benasher44:uuid:${project.property("garbage-uuid-lib-version")}")
            implementation("org.kotlincrypto.hash:sha1:${project.property("kotlin-crypto-version")}")
            // Navigator
            implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")

            // BottomSheetNavigator
            implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")

            // TabNavigator
            implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")

            // Transitions
            implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.framework.datatest)



            }
        }

        val desktopTest by getting {
            dependencies {
                implementation(libs.kotest.runner.junit5.jvm)

            }
        }
    }
}

android {
    namespace = "com.rom4ster.musicmanagerreborn"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.rom4ster.musicmanagerreborn"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/versions/9/*"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.rom4ster.musicmanagerreborn"
            packageVersion = "1.0.0"
        }
    }


}


tasks.matching {
    it.name == "test"
}.configureEach {
    dependsOn("desktopTest")
}




tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
                    events = setOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
            )


        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

//tasks.named<Test>("desktopTest") {
//    outputs.upToDateWhen { false }
//    useJUnitPlatform()
//
//        val specs: String? = System.getProperty("Kotest.filter.specs")
//        val tests: String? = System.getProperty("Kotest.filter.tests")
//        val specPair = specs.takeIf { it != null }?.let { ("kotest_filter_specs" to specs!!) }
//        val testPair = tests.takeIf { it != null }?.let { ("kotest_filter_tests" to tests!!) }
//        val pairs = listOfNotNull(
//            specPair,
//            testPair
//        )
//        println(pairs)
////        if (pairs.isNotEmpty()) {
////            this@named.setEnvironment(
////                *pairs.toTypedArray()
////            )
////        }
//        filter {
//            isFailOnNoMatchingTests = false
//        }
//        testLogging {
//            showExceptions = true
//            showStandardStreams = true
//            events = setOf(
//                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
//                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
//            )
//            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
//        }
//
//}
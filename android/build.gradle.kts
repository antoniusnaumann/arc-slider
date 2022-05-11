plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.foundation:foundation:1.1.1")
    implementation("androidx.compose.material3:material3:1.0.0-alpha10")
    implementation("dev.antonius:chords:0.1.3")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "${project.group}.android"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = project.version as String
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
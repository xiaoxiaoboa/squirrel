plugins {
    kotlin("kapt")
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.squirrel.navigation"
    compileSdk = 34

    defaultConfig {
        minSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        compose  = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion  = "1.5.10"
    }

}
kapt {
    correctErrorTypes = true
}


dependencies {
    implementation(libs.core.ktx)
    implementation(libs.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.andorid.junit)
    implementation(libs.navigation.compose)
    implementation(libs.appcompat)
    implementation(libs.biometric)
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(project(Dependencies.drive))
    implementation(project(Dependencies.utils))
    implementation(project(Dependencies.screens))
}
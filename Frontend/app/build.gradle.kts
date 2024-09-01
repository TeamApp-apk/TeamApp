plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.TeamApp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.TeamApp"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Core AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout)

    // Jetpack Compose dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime) // Use the Compose runtime version you're working with
    implementation(libs.androidx.runtime.livedata) // Ensure it matches the Compose version you're using
    implementation (libs.androidx.material.icons.extended)
    implementation(libs.hilt.android)

    // Navigation
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation (libs.google.accompanist.navigation.animation)
    implementation(libs.accompanist.navigation.animation.v0300)
    implementation (libs.accompanist.navigation.animation.vversion)
    implementation (libs.accompanist.navigation.animation )
    //nie zmieniac tych ponizej, bo jest jakis blad z tymi libsowymi wersjami
    implementation ("com.google.accompanist:accompanist-navigation-animation:0.31.0-alpha")
    implementation("androidx.compose.foundation:foundation-layout-android:1.6.8")
    implementation (libs.androidx.fragment.ktx) // Sprawdź najnowszą wersję
    implementation (libs.androidx.lifecycle.viewmodel.ktx) // Sprawdź najnowszą wersję
    implementation(libs.hilt.android.v244)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Hilt dependencies
    implementation(libs.hilt.android)


    //Animations
    implementation (libs.lottie.compose)


    // Firebase dependencies (using BOM for version management)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database.ktx)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout.compose)
    implementation (libs.androidx.material.icons.extended.v100)
    implementation (libs.play.services.auth)
    implementation (libs.firebase.ui.auth)
    implementation (libs.firebase.auth.v2300)
    implementation (libs.facebook.android.sdk.vlatestrelease)
    implementation (libs.com.facebook.android.facebook.android.sdk)
    implementation (libs.androidx.navigation.compose.v253)



    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug dependencies
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //fonts
    implementation(libs.androidx.ui.text.google.fonts)

    //Google localization services
    implementation ("com.google.android.libraries.places:places:3.5.0")
    implementation ("androidx.compose.ui:ui:1.6.8")
    implementation ("androidx.compose.material:material:1.6.8")
    implementation ("androidx.compose.ui:ui-tooling:1.6.8")
    implementation (libs.androidx.lifecycle.runtime.ktx.v231)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

}
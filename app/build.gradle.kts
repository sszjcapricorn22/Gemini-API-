plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "sszj.s.geminiapi"
    compileSdk = 34

    defaultConfig {
                applicationId = "sszj.s.geminiapi"
                minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // ViewModel and LiveData
    implementation(libs.androidx.lifecycle.viewmodelKtx)
    implementation(libs.androidx.lifecycle.livedataKtx)

    // Fragment KTX
    implementation(libs.androidx.fragment.ktx)

    // Compose Material Icons
    implementation(libs.androidx.compose.material.iconsExtended)
//
    // Compose ViewModel
    implementation(libs.androidx.lifecycle.viewmodelCompose)
//
    // Coil for image loading
    implementation(libs.io.coil.kt.coilCompose)


    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)


// Google AI client
    implementation("com.google.ai.client.generativeai:generativeai:0.8.0")
//GSon
    implementation("com.google.code.gson:gson:2.8.8")

//    implementation ("android.speech:speech-api:29.0.0")
}
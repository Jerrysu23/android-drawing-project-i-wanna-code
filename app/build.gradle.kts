plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services") // Firebase
}

android {
    namespace = "com.example.drawingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.drawingapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
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
    buildFeatures{
        viewBinding = true
        dataBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.github.gcacace:signature-pad:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-testing:2.7.0")
    implementation("androidx.compose.ui:ui-android:1.6.4")
    implementation("androidx.compose.foundation:foundation-android:1.6.4")
    implementation("androidx.compose.material:material-android:1.6.4")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("com.google.firebase:firebase-auth:22.3.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("io.github.vadiole:colorpicker:1.0.4")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.room:room-common:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.8")
    implementation(platform("com.google.firebase:firebase-bom:32.8.0")) // Firebase
    implementation("com.google.firebase:firebase-analytics") // for Firebase packages. we'll probably need more
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-firestore:24.11.1")
    implementation("com.google.firebase:firebase-firestore-ktx:24.11.1")
}
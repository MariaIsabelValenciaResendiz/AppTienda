plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.estructura"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.estructura"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    // ---------- Interfaz (Material Design + AndroidX básico) ----------
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
// ---------- Red: consumir FakeStoreAPI ----------
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
// ---------- Almacenamiento seguro (token cifrado) ----------
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    // ---------- Imágenes de productos ----------
    implementation("com.github.bumptech.glide:glide:4.16.0")
}
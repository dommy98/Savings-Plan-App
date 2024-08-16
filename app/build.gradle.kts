plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
}

android {
    namespace = "com.dominic.savingsplanapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dominic.savingsplanapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        dataBinding = true
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

    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.room.ktx)
    implementation(libs.androidx.databinding.runtime)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.itext7.core)
    implementation (libs.itextpdf)

    implementation (libs.androidx.lifecycle.livedata.ktx)
    annotationProcessor (libs.androidx.room.compiler)
    implementation (libs.androidx.recyclerview)
    implementation (libs.androidx.viewbinding)

    implementation (libs.androidx.activity.ktx)
    implementation (libs.androidx.fragment.ktx)
}

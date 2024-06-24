plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}


android {
    namespace = "com.example.myapplication"
    compileSdk = 34
    //buildFeatures{
   //     viewBinding = true
  //  }

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 29
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

}

dependencies {
    implementation("com.kakao.sdk:v2-user:2.20.1")
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-database-ktx")

    implementation ("com.google.android.gms:play-services-maps:18.0.2") //지도
    implementation ("com.google.firebase:firebase-auth")    //회원가입

    implementation ("com.google.android.gms:play-services-location:21.0.1") //위치
    implementation ("com.google.android.libraries.places:places:3.0.0")

    implementation ("com.google.firebase:firebase-firestore-ktx:24.5.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.messaging.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
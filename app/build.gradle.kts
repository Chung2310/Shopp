plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.shopp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.shopp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.annotation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:okhttp-urlconnection:4.12.0")
    // RxJava
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation ("io.reactivex.rxjava3:rxjava:3.0.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.google.android.material:material:1.12.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.airbnb.android:lottie:6.1.0")


    implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation ("androidx.security:security-crypto:1.1.0-alpha06")

    implementation ("androidx.security:security-identity-credential:1.0.0-alpha03")

}
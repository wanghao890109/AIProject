plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.hao.appcore"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    kapt(libs.arouter.compiler)
    implementation(libs.arouter.api)

    api(libs.androidx.fragment.ktx)
    api(libs.rxjava)
    api(libs.rxandroid)
    api(libs.retrofit)
    api(libs.retrofit.converter.gson)
    api(libs.retrofit.adapter.rxjava2)
    api(libs.whsocket)
    api(libs.okdownload)
    api(libs.channel.helper)
    api(libs.video.processor)

    api(project(":dependencies:common"))
    api(project(":dependencies:service"))
    api(project(":dependencies:ui"))
    api(project(":dependencies:proto"))
    api(project(":dependencies:jsbridge"))
    api(project(":dependencies:permission"))

    implementation("io.github.lucksiege:pictureselector:v3.11.2")

    // image compress library (Not necessary)
    implementation("io.github.lucksiege:compress:v3.11.2")

    // uCrop library (Not necessary)
    implementation("io.github.lucksiege:ucrop:v3.11.2")

    // simple camerax library (Not necessary)
    implementation("io.github.lucksiege:camerax:v3.11.2")
//    api(project(":dependencies:selector"))
//    api(project(":dependencies:compress"))
//    api(project(":dependencies:ucrop"))
}
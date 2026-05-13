plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.hao.ui"
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.material)

    api(libs.androidx.fragment.ktx)

    // Smart Refresh Layout
    api(libs.smart.refresh.layout)
    api(libs.smart.refresh.header.classics)
    api(libs.smart.refresh.header.radar)
    api(libs.smart.refresh.header.falsify)
    api(libs.smart.refresh.header.material)
    api(libs.smart.refresh.header.two.level)
    api(libs.smart.refresh.footer.ball)
    api(libs.smart.refresh.footer.classics)

    api(libs.svgaplayer)

    // Animation
    api(libs.animation.awebp)
    api(libs.animation.gif)
    api(libs.animation.apng)
    api(libs.animation.glide.plugin)

    // ImmersionBar
    api(libs.immersionbar)
    api(libs.immersionbar.ktx)

    // OkHttp
    api(libs.okhttp.logging)
    api(libs.okhttp)
    api(libs.okhttp.mockwebserver)

    // Glide
    api(libs.glide)
    kapt(libs.glide.compiler)
    api(libs.glide.transformations)

    // RxLifecycle
    api(libs.rxlifecycle)
    api(libs.rxlifecycle.components)

    api(libs.banner)
    api(libs.flyco.roundview)

    // ARouter
    api(libs.arouter.api)
    kapt(libs.arouter.compiler)

    api(project(":dependencies:common"))
    api(project(":dependencies:quickadapter"))
    api(project(":dependencies:statemanager"))
}
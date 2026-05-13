plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.hao.proto"
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

    kapt(libs.arouter.compiler)
    implementation(libs.arouter.api)

    api("com.squareup.wire:wire-runtime:3.5.0")
}



tasks.register<JavaExec>("_proto") {
    doFirst {
        val gitURL = "git@github.com:404536204/proto.git"
        exec {
            commandLine("sh", "./proto.sh", gitURL)
        }
    }

    classpath = files("tools/wire-compiler-3.6.0-SNAPSHOT-jar-with-dependencies.jar")
    mainClass = "com.squareup.wire.WireCompiler"
    args = listOf(
        "--proto_path=file",
        "--java_out=src/main/java"
    )
}
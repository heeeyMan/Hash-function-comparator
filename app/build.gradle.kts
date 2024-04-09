plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.hfc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hfc"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        externalNativeBuild {
            cmake {
                cppFlags("-std=c++20")
                //arguments("-Daidl_src_dir=${arrayOf(project(":Common").projectDir.absolutePath, "src", "main", "cpp", "aidl").joinToString(File.separator).replace("\\","/")}",
                //"-Dcommon_inc_dir=${arrayOf(project(":Common").projectDir.absolutePath, "src", "main", "cpp", "includes").joinToString(File.separator).replace("\\","/")}")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    externalNativeBuild {
        cmake {
            path = File("src/main/cpp/CMakeLists.txt")
            version = "3.6.0"
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
        compose = true
        aidl = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.create("compileAidlNdk") {
    doLast {
        // Path to aidl tool
        val aidl = arrayOf<String>(
            project.android.sdkDirectory.absolutePath,
            "build-tools", project.android.buildToolsVersion, "aidl"
        ).joinToString(File.separator)

        val outDir = arrayOf<String>(
            project.projectDir.absolutePath, "src", "main", "cpp", "aidl"
        ).joinToString(File.separator)

        val headerOutDir = arrayOf<String>(
            project.projectDir.absolutePath, "src", "main", "cpp", "includes"
        ).joinToString(File.separator)

        val searchPathForImports = arrayOf<String>(
            project.projectDir.absolutePath, "src", "main", "aidl"
        ).joinToString(File.separator)

        val aidlFile = arrayOf<String>(
            project.projectDir.absolutePath, "src", "main", "aidl",
            "com", "example", "IMyService.aidl"
        ).joinToString(File.separator)

        // Exec aidl command to generate headers and source files for IMyService.aidl
        ProcessBuilder(aidl,
            "--lang=ndk",
            "-o", outDir,
            "-h", headerOutDir,
            "-I", searchPathForImports,
            aidlFile
        ).start().waitFor()
    }
}

// To generate headers and source files for IMyService.aidl before each build
afterEvaluate {
    tasks.named("preBuild") {
        dependsOn(tasks.named("compileAidlNdk"))
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    //compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    // tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // se agrega ksp
    id("com.google.devtools.ksp")
}

android {
    namespace = "cl.jmcontrerass.android.examen"
    compileSdk = 34

    defaultConfig {
        applicationId = "cl.jmcontrerass.android.examen"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    // versión de material 3
    implementation("androidx.compose.material3:material3:1.0.1")
    // coil
    implementation("io.coil-kt:coil-compose:2.5.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // view model
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    // CameraX
    val camerax_version = "1.2.3"
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    // Si se necesita CameraX View classs
    implementation("androidx.camera:camera-view:${camerax_version}")
    // Si se necesita la libería para gestion del ciclo vida
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    // Si se necesita hacer captura de video
    //implementation("androidx.camera:camera-video:${camerax_version}")
    // Si se quiere usar el Kit de Machine Learning
    //implementation("androidx.camera:camera-mlkitvision:${camerax_version}")
    // Si se quiere utilizar las extensiones de CameraX
    //implementation("androidx.camera:camera-extensions:${camerax_version}")

    // ubicación google play
    implementation("com.google.android.gms:play-services-location:21.0.1")
    // open street map (osmdroid)
    implementation("org.osmdroid:osmdroid-android:6.1.16")

    // ROOM
    val room_version = "2.5.0"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    // kapt("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // optional - RxJava2 support for Room
    // implementation("androidx.room:room-rxjava2:$room_version")

    // optional - RxJava3 support for Room
    // implementation("androidx.room:room-rxjava3:$room_version")

    // optional - Guava support for Room, including Optional and ListenableFuture
    // implementation("androidx.room:room-guava:$room_version")

    // optional - Test helpers
    // testImplementation("androidx.room:room-testing:$room_version")

    // optional - Paging 3 Integration
    // implementation("androidx.room:room-paging:$room_version")
    
    // retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.13.0")
}
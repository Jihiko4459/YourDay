plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization) // Добавьте плагин для Kotlin Serialization
    alias(libs.plugins.kotlin.kapt)

}

android {
    namespace = "com.example.yourday"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.yourday"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}
// Room - база данных на телефоне (как Excel, но для приложений)
val room_version = "2.6.1" // всегда бери последнюю стабильную версию


dependencies {


    //библиотека Coil для загрузки изображений по URL
    implementation("io.coil-kt:coil-compose:2.4.0") // Последняя версия Coil

    // Сама библиотека Room
    implementation("androidx.room:room-runtime:$room_version")
    // Дополнительно - поддержка Kotlin корутин для Room
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:2.6.1")


    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")



    // Supabase (используйте одинаковые версии для всех модулей!)

    implementation("io.github.jan-tennert.supabase:postgrest-kt:2.2.1")
    implementation("io.github.jan-tennert.supabase:gotrue-kt:2.2.1")
    implementation("io.github.jan-tennert.supabase:storage-kt:2.2.1") // Обновите до той же версии
    implementation("io.github.jan-tennert.supabase:realtime-kt:2.2.1") // Опционально, для realtime-функций
    implementation("io.ktor:ktor-client-okhttp:2.3.7") // HTTP-клиент (совместим с 2.2.1)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0") // Для сериализации

    implementation("com.google.code.gson:gson:2.10.1")



    implementation("androidx.preference:preference-ktx:1.2.1")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")


    implementation("androidx.activity:activity-compose:1.7.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
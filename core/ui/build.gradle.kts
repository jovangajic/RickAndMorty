plugins {
    alias(libs.plugins.rickandmorty.android.library.compose)
}

android {
    namespace = "rs.jovan.rickandmorty.core.ui"
}

dependencies {
    api(project(":core:domain"))

    implementation(libs.coil.compose)
}

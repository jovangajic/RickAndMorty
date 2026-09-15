plugins {
    alias(libs.plugins.rickandmorty.android.feature)
}

android {
    namespace = "rs.jovan.rickandmorty.feature.favorites"
}

dependencies {
    testImplementation(libs.mockk)
}

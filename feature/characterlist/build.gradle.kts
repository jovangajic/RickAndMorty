plugins {
    alias(libs.plugins.rickandmorty.android.feature)
}

android {
    namespace = "rs.jovan.rickandmorty.feature.characterlist"
}

dependencies {
    implementation(libs.paging.compose)

    testImplementation(libs.mockk)
}

plugins {
    alias(libs.plugins.rickandmorty.jvm.library)
}

dependencies {
    // PagingData + Flow are part of the repository contract
    api(libs.paging.common)
}

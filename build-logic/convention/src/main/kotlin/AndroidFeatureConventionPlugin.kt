import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A feature module: Compose UI + Hilt ViewModels + a type-safe navigation entry point.
 * Features depend only on core abstractions, never on each other or on :core:data.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("rickandmorty.android.library.compose")
            pluginManager.apply("rickandmorty.hilt")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                "implementation"(project(":core:domain"))
                "implementation"(project(":core:ui"))

                "implementation"(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                "implementation"(libs.findLibrary("androidx-navigation-compose").get())
                "implementation"(libs.findLibrary("hilt-navigation-compose").get())
                "implementation"(libs.findLibrary("kotlinx-serialization-json").get())
            }
        }
    }
}

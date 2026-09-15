import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("rickandmorty.android.library")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
            }

            dependencies {
                val bom = libs.findLibrary("androidx-compose-bom").get()
                "implementation"(platform(bom))
                "implementation"(libs.findLibrary("androidx-compose-ui").get())
                "implementation"(libs.findLibrary("androidx-compose-ui-graphics").get())
                "implementation"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
                "implementation"(libs.findLibrary("androidx-compose-material3").get())
                "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
                "androidTestImplementation"(platform(bom))
            }
        }
    }
}

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.android")

            extensions.configure<LibraryExtension> {
                compileSdk = 36

                defaultConfig {
                    minSdk = 28
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = JAVA_VERSION
                    targetCompatibility = JAVA_VERSION
                }
            }
            configureKotlinJvmTarget()

            dependencies {
                "testImplementation"(libs.findLibrary("junit").get())
            }
        }
    }
}

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal val JAVA_VERSION = JavaVersion.VERSION_17

internal fun Project.configureKotlinJvmTarget() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
}

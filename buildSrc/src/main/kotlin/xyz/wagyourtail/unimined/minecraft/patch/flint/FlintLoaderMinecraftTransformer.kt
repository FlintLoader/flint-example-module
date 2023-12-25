package xyz.wagyourtail.unimined.minecraft.patch.flint

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import xyz.wagyourtail.unimined.api.minecraft.EnvType
import xyz.wagyourtail.unimined.api.runs.RunConfig
import xyz.wagyourtail.unimined.internal.minecraft.MinecraftProvider
import xyz.wagyourtail.unimined.internal.minecraft.patch.MinecraftJar
import xyz.wagyourtail.unimined.internal.minecraft.patch.fabric.FabricLikeMinecraftTransformer
import java.io.InputStreamReader
import java.net.URI
import java.nio.file.Files

class FlintLoaderMinecraftTransformer(
        project: Project,
        provider: MinecraftProvider
): FabricLikeMinecraftTransformer(
        project,
        provider,
        "flint",
        "flintmodule.json",
        "accessWidener"
) {
    override val ENVIRONMENT: String
        get() = TODO("Not yet implemented")
    override val ENV_TYPE: String
        get() = TODO("Not yet implemented")

    override fun addIncludeToModJson(json: JsonObject, dep: Dependency, path: String) {
        TODO("Not yet implemented")
    }

    init {
        provider.side = EnvType.CLIENT;
    }

    override fun addIntermediaryMappings() {
        provider.mappings {
            intermediary()
        }
    }

    override fun collectInterfaceInjections(baseMinecraft: MinecraftJar, injections: HashMap<String, List<String>>) {
        val modJsonPath = this.getModJsonPath()

        if (modJsonPath != null && modJsonPath.exists()) {
            val json = JsonParser.parseReader(InputStreamReader(Files.newInputStream(modJsonPath.toPath()))).asJsonObject

            val custom = json.getAsJsonObject("custom")

            if (custom != null) {
                val interfaces = custom.getAsJsonObject("steel:injected_interfaces")

                if (interfaces != null) {
                    collectInterfaceInjections(baseMinecraft, injections, interfaces)
                }
            }
        }
    }

    override fun loader(dep: Any, action: Dependency.() -> Unit) {
        fabric.dependencies.add(
                (if (dep is String && !dep.contains(":")) {
                    project.dependencies.create("net.flintloader:punch:$dep")
                } else project.dependencies.create(dep)).apply(action)
        )
    }

    override fun addMavens() {
        project.repositories.maven {
            this.name = "flint"
            this.url = URI.create("https://maven.flintloader.net/releases")
        }

        project.repositories.maven {
            this.name = "flintmirror"
            this.url = URI.create("https://maven.flintloader.net/mirror")
        }
    }

    override fun applyExtraLaunches() {
        super.applyExtraLaunches()
        if (provider.side == EnvType.SERVER) {
            throw RuntimeException("Flint Loader does not support Server Sided installs")
        }
    }

    override fun applyClientRunTransform(config: RunConfig) {
        super.applyClientRunTransform(config)
        config.jvmArgs += listOf(
                "-Dflint.development=true",
                "-Dflint.remapClasspathFile=${intermediaryClasspath}",
                "-Dflint.classPathGroups=${groups}"
        )
    }
}
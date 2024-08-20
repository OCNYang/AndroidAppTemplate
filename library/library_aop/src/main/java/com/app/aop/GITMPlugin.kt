package com.app.aop

import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.app.aop.click.ComposeClickClassVisitorFactory
import com.app.aop.click.ComposeClickConfig
import com.app.aop.click.ComposeClickPluginParameter
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Ghost In The Machine
 */
class GITMPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("composeClickGITM", ComposeClickPluginParameter::class.java)

        project.extensions.getByType(AndroidComponentsExtension::class.java).onVariants { variant ->

            project.extensions.findByType(ComposeClickPluginParameter::class.java)?.let {
                variant.instrumentation.transformClassesWith(
                    ComposeClickClassVisitorFactory::class.java,
                    InstrumentationScope.ALL,
                    instrumentationParamsConfig = { params -> params.config.set(ComposeClickConfig(it)) }
                )
            }

        }
    }
}
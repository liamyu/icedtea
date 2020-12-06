package io.github.liamyu

import org.gradle.api.Plugin
import org.gradle.api.Project

class AsmPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("io.github.liamyu.AsmPlugin.apply() called.")
    }
}
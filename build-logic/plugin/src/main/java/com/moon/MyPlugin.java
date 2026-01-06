package com.moon;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyPlugin implements Plugin<Project>  {

    @Override
    public void apply(Project project) {
        project.getPlugins().apply("java");
        project.getDependencies().add("compileOnly", "org.projectlombok:lombok");
        project.getDependencies().add("annotationProcessor", "org.projectlombok:lombok");
        project.getDependencies().add("testImplementation", "org.springframework.boot:spring-boot-starter-test");
        project.getDependencies().add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher");
    }
}

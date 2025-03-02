import org.springframework.boot.buildpack.platform.build.PullPolicy
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
  java
  id("org.springframework.boot") version "3.4.3"
  id("io.spring.dependency-management") version "1.1.7"
}

group = "io.github.goatfryed"
version = properties["version"]!!

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(23)
  }
}

repositories {
  mavenCentral()
}

dependencyManagement {
  imports {
    mavenBom("de.codecentric:spring-boot-admin-dependencies:3.4.4")
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
  }
}

dependencies {
  implementation("de.codecentric:spring-boot-admin-starter-client")
  implementation("de.codecentric:spring-boot-admin-starter-server")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.cloud:spring-cloud-starter")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

val runImageName = properties["runImageName"]?.toString()

tasks.register<Exec>("buildRunImage") {
  group = "build"
  description = "Builds the base run image for the application"
  commandLine(
    "docker", "build",
    "-t", runImageName,
    "${projectDir}/docker/run-image"
  )
}

tasks.named<BootBuildImage>("bootBuildImage") {
  val imageRef = "goatfryed/${project.name}"
  imageName.set("$imageRef:${project.version}")
  if (properties["buildPackPullPolicy"] != "always") {
    pullPolicy.set(PullPolicy.IF_NOT_PRESENT)
  }
  runImage.set(runImageName)
}
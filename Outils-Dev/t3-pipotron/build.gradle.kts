plugins {
    application
}

application {
    mainClass.set("pipotron.Pipotron")
}

tasks.withType<JavaExec> {
    jvmArgs = listOf("-Dstdout.encoding=UTF-8", "-Dstderr.encoding=UTF-8")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:33.7.1-jre")
    implementation("tools.jackson.core:jackson-databind:3.2.2")
}
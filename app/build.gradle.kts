plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(libs.guava)
    implementation("uk.co.electronstudio.jaylib:jaylib:5.0.+")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "me.tim.Main"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

plugins {
    kotlin("jvm") version "1.9.22"
    id("idea")
    `maven-publish`
    `java-library`
    id("org.jreleaser") version "1.12.0"
}


group = "io.github.nikotung"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
}

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}

val snapshot = "snapshot"
val release = "release"
publishing {
    fun MavenPom.initPom() {
        name.set("emv-qrcode-kotlin")
        description.set("A Kotlin library for parsing EMV QR Codes")
        url.set("https://github.com/nikotung/emv-qrcode-kotlin")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        scm {
            url.set("https://github.com/nikotung/emv-qrcode-kotlin.git")
        }
        developers {
            developer {
                id.set("nikotung")
                name.set("Niko Tung")
                email.set("n@eiko.me")
            }
        }
    }

    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }

    publications {
        create<MavenPublication>(release) {
            version = "${project.version}"

            from(components["java"])

            pom.initPom()
        }
    }
}



jreleaser {
    signing {
        setActive("ALWAYS")
        armored = true
    }
    release {
        github {
            enabled = false
        }
    }

    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    setActive("ALWAYS")
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
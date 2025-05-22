plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    id("maven-publish") // Make sure this line is present
    id("signing")
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0" apply false
    id("com.vanniktech.maven.publish") version "0.25.3" apply false
    id("org.jetbrains.dokka") version "1.9.10" // or latest
}

android {
    namespace = "com.famdigitalindonesia.librarysample1"
    compileSdk = 35

    defaultConfig {
//        applicationId = "com.famdigitalindonesia.librarysample1"
        minSdk = 24
        targetSdk = 35
//        versionCode = 1
//        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.famdigitalindonesia"
                artifactId = "test-library"
                version = "1.0.0"
//                artifact(sourcesJar.get())
//                artifact(javadocJar.get())
                // pom(splitPOM)

                repositories {
//                    maven {
//                        name = "ReleaseRepo"
//                        url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
//
//                        credentials {
//                            username = properties.get("mavenCentralUsername") as String?
//                            password = properties.get("mavenCentralPassword") as String?
//                            println("username: $username")
//                            println("username: $password")
//                        }
//                    }

                    maven {
                        name = "ReleaseRepo"
                        url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                        credentials {
                            username = findProperty("mavenCentralUsername")?.toString() ?: System.getenv("OSSRH_USERNAME")
                            password = findProperty("mavenCentralPassword")?.toString() ?: System.getenv("OSSRH_PASSWORD")
                        }
                    }

                    maven {
                        name = "sonatypeSnapshots"
                        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                        credentials {
                            username = findProperty("mavenCentralUsername")?.toString() ?: System.getenv("OSSRH_USERNAME")
                            password = findProperty("mavenCentralPassword")?.toString() ?: System.getenv("OSSRH_PASSWORD")
                        }
                    }
                }

                // artifact("${rootProject.projectDir}/app/build/entou/${project.name}-release.aar")
//                 artifact("$buildDir/outputs/aar/${project.name}-release.aar")
                // artifact("$buildDir/outputs/aar/app-release.aar")
            }
            create<MavenPublication>("development") {
                from(components["release"])
                groupId = "com.famdigitalindonesia"
                artifactId = "test-library"
                version = "1.0.0"
            }
        }

        repositories {
            maven {
                println("Message buildDir: ${buildDir}")  // String interpolation
                println("Message rootProject: ${rootProject}")  // String interpolation
                println("Message rootDir: ${rootDir}")  // String interpolation
                println("Message rootProject: ${rootProject}")  // String interpolation
                println("Message rootProject.buildDir: ${rootProject.buildDir}")  // String interpolation
                println("Message rootProject.projectDir: ${rootProject.projectDir}")  // String interpolation
                url = uri("${rootProject.projectDir}/app/build")
            }
        }
    }
    signing {
        useGpgCmd()
        sign(publishing.publications["release"])
        sign(configurations.getByName("archives"))
    }

    tasks.register<PublishToMavenRepository>("publishRelease") {
        publication = publishing.publications["release"] as MavenPublication
        repository = publishing.repositories.named<MavenArtifactRepository>("ReleaseRepo").get()
    }

    tasks.register<PublishToMavenRepository>("publishDev") {
        publication = publishing.publications["release"] as MavenPublication
        repository = publishing.repositories.named<MavenArtifactRepository>("DevelopmentRepo").get()
    }
}
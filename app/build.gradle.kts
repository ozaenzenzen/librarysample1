plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    id("maven-publish") // Make sure this line is present
}

android {
    namespace = "com.famdigitalindonesia.librarysample1"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35

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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
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

val splitPOM: MavenPom.() -> Unit = {
    name.set("Library Sample SDK")
    packaging = "aar"
    description.set("Official Library Sample Android SDK")
    url.set("https://github.com/ozaenzenzen/librarysample1")

    licenses {
        license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        }
    }

    developers {
        developer {
            id.set("ozaenzenzen")
            name.set("Fauzan Akmal Mahdi")
            email.set("recovery252@gmail.com")
        }
    }

    scm {
        connection.set("scm:git:git@github.com:ozaenzenzen/librarysample1.git")
        developerConnection.set("scm:git@github.com:ozaenzenzen/librarysample1.git")
        url.set("https://github.com/ozaenzenzen/librarysample1.git")
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.ozaenzenzen"
                artifactId = "librarysample1"
                version = "1.0.3"
//                pom(splitPOM)
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
}
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            plugin("android-application", "com.android.application").version("8.2.2")
            plugin("kotlin-android", "org.jetbrains.kotlin.android").version("1.9.22")
            plugin("compose-compiler", "androidx.compose.compiler").version("1.5.8")
            
            library("androidx-core-ktx", "androidx.core:core-ktx:1.12.0")
            library("androidx-appcompat", "androidx.appcompat:appcompat:1.6.1")
            library("material", "com.google.android.material:material:1.11.0")
            library("junit", "junit:junit:4.13.2")
            library("androidx-junit", "androidx.test.ext:junit:1.1.5")
            library("androidx-espresso-core", "androidx.test.espresso:espresso-core:3.5.1")
        }
    }
}

rootProject.name = "hearbook"
include(":app")
 
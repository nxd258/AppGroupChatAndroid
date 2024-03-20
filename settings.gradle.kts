pluginManagement {
    repositories {
        google()
        mavenCentral()
        jcenter(){
            content(){
                includeModule("com.theartofdev.edmodo","android-image-cropper")
            }
        }
        maven { url = uri("https://jitpack.io") }



        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter(){
            content(){
                includeModule("com.theartofdev.edmodo","android-image-cropper")
            }
        }
        maven { url = uri("https://jitpack.io") }

    }
}

rootProject.name = "doan-appchat"
include(":app")
 
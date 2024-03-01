pluginManagement {
    repositories {
        google()
//        maven ("https://maven.aliyun.com/repository/google")
//        maven ("https://maven.aliyun.com/repository/jcenter")
//        maven ("https://maven.aliyun.com/repository/public")
        maven ("https://maven.aliyun.com/repository/gradle-plugin")
        maven ("https://maven.aliyun.com/nexus/content/groups/public")
        maven ("https://maven.aliyun.com/nexus/content/repositories/jcenter")
        maven ("https://maven.aliyun.com/nexus/content/repositories/google")
        maven { setUrl("https://jitpack.io") }
        mavenCentral { setUrl("https://jcenter.bintray.com/") }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
//        maven ("https://maven.aliyun.com/repository/google")
//        maven ("https://maven.aliyun.com/repository/jcenter")
//        maven ("https://maven.aliyun.com/repository/public")
        maven ("https://maven.aliyun.com/repository/gradle-plugin")
        maven ("https://maven.aliyun.com/nexus/content/groups/public")
        maven ("https://maven.aliyun.com/nexus/content/repositories/jcenter")
        maven ("https://maven.aliyun.com/nexus/content/repositories/google")
        maven { setUrl("https://jitpack.io") }
        mavenCentral { setUrl("https://jcenter.bintray.com/") }
        mavenCentral()
    }
}

rootProject.name = "HairStyle"
include(":app")

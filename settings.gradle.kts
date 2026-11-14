// 只保留 pluginManagement。
// 关键：不要在这里声明 dependencyResolutionManagement 的 repositoriesMode，
// 否则会覆盖 Kotlin 插件自动注入的 nodejs.org（Ivy）仓库，
// 导致 org.nodejs:node 在 Maven 里找不到而失败。
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "kmp-demo"
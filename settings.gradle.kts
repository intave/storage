rootProject.name = "intavestorage"
include("storage-mysql")
include("storage-plugin")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
include("storage-postgresql")
include("storage-mariadb")
include("storage-mongodb")

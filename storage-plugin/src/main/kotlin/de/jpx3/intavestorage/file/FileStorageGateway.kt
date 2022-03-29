package de.jpx3.intavestorage.file

import de.jpx3.intavestorage.ExpiringStorageGateway
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class FileStorageGateway : ExpiringStorageGateway {
    override fun requestStorage(id: UUID, consumer: Consumer<ByteBuffer>) {
        val file = fileOf(id)
        val bytes = FileInputStream(file).use(FileInputStream::readBytes)
        consumer.accept(ByteBuffer.wrap(bytes))
    }

    override fun saveStorage(id: UUID, buffer: ByteBuffer) {
        val file = fileOf(id)
        FileOutputStream(file).use { it.write(buffer.array()) }
    }

    private fun fileOf(id: UUID): File {
        val prefix = id.toString().substring(0, 2)
        val file = File(cacheFile(), "$prefix/$id.storage")

        try {
            file.parentFile.mkdirs()
            if (!file.exists() && !file.createNewFile()) {
                error("Failed to create file ${file.absolutePath}")
            }
        } catch (e: Exception) {
            throw IllegalStateException("Failed to create file ${file.absolutePath}", e)
        }

        return file
    }

    override fun clearEntriesOlderThan(value: Long, unit: TimeUnit) {
        cacheFile()
            .walkTopDown()
            .filter { file -> file.isFile && file.name.endsWith(".storage") }
            .filter { file -> System.currentTimeMillis() - file.lastModified() > unit.toMillis(value) }
            .forEach(File::delete)
    }

    private fun cacheFile(): File {
        val operatingSystem = System.getProperty("os.name").lowercase()
        val filePath = if (operatingSystem.contains("win")) {
            System.getenv("APPDATA") + "/Intave/Storage/"
        } else {
            System.getProperty("user.home") + "/.intave/storage/"
        }
        val workDirectory = File(filePath)
        if (!workDirectory.exists()) {
            workDirectory.mkdir()
        }
        return workDirectory
    }
}

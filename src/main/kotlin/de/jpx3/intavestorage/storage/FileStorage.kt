package de.jpx3.intavestorage.storage

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class FileStorage : ClearableStorageGateway {
  override fun requestStorage(id: UUID?, consumer: Consumer<ByteBuffer>?) {
    if (id == null || consumer == null) {
      return
    }
    println("read request for $id")
    val file = fileOf(id)
    consumer.accept(ByteBuffer.wrap(FileInputStream(file).readBytes()))
  }

  override fun saveStorage(id: UUID?, buffer: ByteBuffer?) {
    if (id == null || buffer == null) {
      return
    }
    println("save request for $id $buffer")
    val file = fileOf(id)
    val output = FileOutputStream(file)
    output.write(buffer.array())
  }

  private fun fileOf(id: UUID): File {
    val prefix = id.toString().substring(0, 2)
    val file = File(cacheFile(),"$prefix/$id.storage")
    try {
      file.parentFile.mkdirs()
      if (!file.exists() && !file.createNewFile()) {
        error("Failed to create file ${file.absolutePath}")
      }
    } catch (exception: Exception) {
      exception.printStackTrace()
      error("Failed to create file ${file.absolutePath}")
    }
    return file
  }

  override fun clearEntriesOlderThan(value: Long, unit: TimeUnit) {
    File("${cacheFile()}")
      .walkTopDown()
      .filter { file -> file.isFile && file.name.endsWith(".storage") }
      .filter { file -> System.currentTimeMillis() - file.lastModified() > unit.toMillis(value)}
      .forEach { file -> file.delete() }
  }

  private fun cacheFile(): File {
    val operatingSystem = System.getProperty("os.name").lowercase()
    val workDirectory: File
    val filePath: String = when {
      operatingSystem.contains("win") -> {
        System.getenv("APPDATA") + "/Intave/Storage/"
      }
      else -> System.getProperty("user.home") + "/.intave/storage/"
    }
    workDirectory = File(filePath)
    if (!workDirectory.exists()) {
      workDirectory.mkdir()
    }
    return workDirectory
  }
}

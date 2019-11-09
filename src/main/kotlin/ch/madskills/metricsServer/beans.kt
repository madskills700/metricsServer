package ch.madskills.metricsServer

import oshi.SystemInfo
import java.time.OffsetDateTime

/** Data-class for metrics */
data class Metrics(
        val cpuTemp: Double,
        val cpuLoad: Double,
        val ramFree: Double,
        val ramTotal: Double,
        val totalDiskSpace: Double,
        val freeDiskSpace: Double,
        val created: OffsetDateTime
)
/** Data-class for store file properties */
data class FileProperties(
        val name: String,
        val size: Double,
        val created: OffsetDateTime,
        val changed: OffsetDateTime
)

/** Тестовый класс метрик */
fun main() {
    val sysInfo = SystemInfo()
    println(sysInfo.hardware.sensors.cpuTemperature)
    println(sysInfo.hardware.processor.getSystemLoadAverage(3)[1])
    println(sysInfo.hardware.memory.available.toDouble() / 1024 / 1024 / 1024)
    println(sysInfo.hardware.memory.total.toDouble() / 1024 / 1024 / 1024)
    println(sysInfo.operatingSystem.fileSystem.fileStores[0].totalSpace.toDouble() / 1000 / 1024 / 1024)
    println(sysInfo.operatingSystem.fileSystem.fileStores[0].usableSpace.toDouble() / 1000 / 1024 / 1024)
}
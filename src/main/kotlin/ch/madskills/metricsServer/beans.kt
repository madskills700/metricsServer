package ch.madskills.metricsServer

/** Data-class для сущности metrics */
data class Metrics(
        val cpuTemp: Double,
        val cpuLoad: Double,
        val ramFree: Double,
        val ramTotal: Double,
        val totalDiskSpace: Double,
        val freeDiskSpace: Double,
        val created: Long
)

/** Data-class для сущности файла со свойствами */
data class FileProperties(
        val name: String,
        val size: Double,
        val isDirectory: Boolean,
        val isExecutable: Boolean,
        val changed: Long
)

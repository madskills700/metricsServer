package ch.madskills.metricsServer

import org.jetbrains.exposed.sql.Table

object MetricsTable : Table("system_metrics") {
    val cpuTemp = double("cpuTemp")
    val cpuLoad = double("cpuLoad")
    val ramFree = double("ramFree")
    val ramTotal = double("ramTotal")
    val totalDiskSpace = double("totalDiskSpace")
    val freeDiskSpace = double("freeDiskSpace")
    val created = datetime("created")
}
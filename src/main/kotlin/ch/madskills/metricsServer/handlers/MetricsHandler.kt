package ch.madskills.metricsServer.handlers

import ch.madskills.metricsServer.Metrics
import com.fasterxml.jackson.databind.ObjectMapper
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import io.undertow.util.HttpString
import org.slf4j.LoggerFactory
import oshi.SystemInfo

/**
 * Класс-хендлер на запрос метрик
 * @author madskills700
 */
class MetricsHandler : HttpHandler {

    /** Логгер */
    private val logger = LoggerFactory.getLogger(javaClass)
    /** Маппер json */
    private val mapper : ObjectMapper = ObjectMapper()

    override fun handleRequest(exchange: HttpServerExchange) {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this)
            return
        }

        val sysInfo = SystemInfo()
        val cpuTemp = sysInfo.hardware.sensors.cpuTemperature
        val cpuLoad = sysInfo.hardware.processor.getSystemLoadAverage(3)[1]
        val ramFree = sysInfo.hardware.memory.available.toDouble() / 1024 / 1024 / 1024
        val ramTotal = sysInfo.hardware.memory.total.toDouble() / 1024 / 1024 / 1024
        val totalDiskSpace = sysInfo.operatingSystem.fileSystem.fileStores[0].totalSpace.toDouble() / 1000 / 1024 / 1024
        val freeDiskSpace = sysInfo.operatingSystem.fileSystem.fileStores[0].usableSpace.toDouble() / 1000 / 1024 / 1024
        val created = System.currentTimeMillis()

        exchange.responseHeaders.add(HttpString("content-type"), "application/json")
        exchange.responseSender.send(mapper.writeValueAsString(Metrics(cpuTemp, cpuLoad, ramFree, ramTotal, totalDiskSpace, freeDiskSpace, created)))
    }

}
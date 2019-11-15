package ch.madskills.metricsServer.handlers

import ch.madskills.metricsServer.Metrics
import ch.madskills.metricsServer.MetricsTable
import com.fasterxml.jackson.databind.ObjectMapper
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import io.undertow.util.HttpString
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

/**
 * Класс-хендлер на запрос метрик
 * @author madskills700
 */
class MetricsHandler : HttpHandler {

    /** Логгер */
    private val logger = LoggerFactory.getLogger(javaClass)
    /** Маппер json */
    private val mapper: ObjectMapper = ObjectMapper()

    override fun handleRequest(exchange: HttpServerExchange) {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this)
            return
        }

        val metricsList = ArrayList<Metrics>()
        transaction {
            val query = MetricsTable.selectAll().limit(500)
            metricsList.addAll(query.map { it.toMestrics() })
        }

        exchange.responseHeaders.add(HttpString("content-type"), "application/json")
        exchange.responseSender.send(mapper.writeValueAsString(metricsList))
    }

    fun ResultRow.toMestrics() = Metrics(
            cpuTemp = this[MetricsTable.cpuTemp],
            cpuLoad = this[MetricsTable.cpuLoad],
            ramFree = this[MetricsTable.ramFree],
            ramTotal = this[MetricsTable.ramTotal],
            totalDiskSpace = this[MetricsTable.totalDiskSpace],
            freeDiskSpace = this[MetricsTable.freeDiskSpace],
            created = this[MetricsTable.created].millis
    )
}
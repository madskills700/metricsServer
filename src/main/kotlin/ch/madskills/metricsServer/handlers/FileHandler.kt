package ch.madskills.metricsServer.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import org.slf4j.LoggerFactory

/**
 * Класс, реализующий хендлер на запрос списка файлов в директории
 * @author madskills700
 */
class FileHandler : HttpHandler {

    /** Логгер */
    private val logger = LoggerFactory.getLogger(javaClass)
    /** Маппер json */
    private val mapper: ObjectMapper = ObjectMapper()

    override fun handleRequest(exchange: HttpServerExchange) {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this)
            return
        }
        logger.info("handle")
        exchange.endExchange()
    }

}
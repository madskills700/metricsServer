package ch.madskills.metricsServer.handlers

import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import org.slf4j.LoggerFactory

class MetricsHandler : HttpHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handleRequest(exchange: HttpServerExchange?) {
        logger.info("handle")
        exchange?.endExchange()
    }

}
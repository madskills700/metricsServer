package ch.madskills.metricsServer.handlers

import ch.madskills.metricsServer.FileProperties
import ch.madskills.metricsServer.props
import com.fasterxml.jackson.databind.ObjectMapper
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import io.undertow.util.HttpString
import org.slf4j.LoggerFactory
import java.io.File

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
        logger.info("handle file request")
        val files = ArrayList<FileProperties>()

        File(props.getProperty("directoryToScan")).walkTopDown().maxDepth(1).forEach {
            val name = it.name
            val size = it.length().toDouble() / 1024
            val isExecutable = it.canExecute()
            val isDirectory = it.isDirectory
            val changed = it.lastModified()
            files.add(FileProperties(name, size, isDirectory, isExecutable, changed))
        }
        exchange.responseHeaders.add(HttpString("content-type"), "application/json")
        exchange.responseSender.send(mapper.writeValueAsString(files))
    }

}
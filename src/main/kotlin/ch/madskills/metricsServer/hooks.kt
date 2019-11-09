package ch.madskills.metricsServer

import ch.madskills.metricsServer.handlers.FileHandler
import ch.madskills.metricsServer.handlers.MetricsHandler
import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.Option
import com.jayway.jsonpath.spi.json.JacksonJsonProvider
import com.jayway.jsonpath.spi.json.JsonProvider
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider
import com.jayway.jsonpath.spi.mapper.MappingProvider
import com.networknt.handler.HandlerProvider
import com.networknt.handler.LightHttpHandler.logger
import com.networknt.server.ShutdownHookProvider
import com.networknt.server.StartupHookProvider
import io.undertow.Handlers
import io.undertow.server.HttpHandler
import io.undertow.util.Methods
import org.slf4j.LoggerFactory
import java.util.*

private val logger = LoggerFactory.getLogger("Hooks")

class StartupHook: StartupHookProvider {

    override fun onStartup() {
        configJsonPath()
        logger.info("Startup hook started")
    }

    internal fun configJsonPath() {
        Configuration.setDefaults(object : Configuration.Defaults {

            private val jsonProvider = JacksonJsonProvider()
            private val mappingProvider = JacksonMappingProvider()

            override fun jsonProvider(): JsonProvider {
                return jsonProvider
            }

            override fun mappingProvider(): MappingProvider {
                return mappingProvider
            }

            override fun options(): Set<Option> {
                return EnumSet.noneOf(Option::class.java)
            }
        })
    }
}

class ShutdownHook: ShutdownHookProvider {

    override fun onShutdown() {
        logger.info("Shutdown hook started")
    }

}

class PathHandlerProvider: HandlerProvider {

    override fun getHandler(): HttpHandler {
        return Handlers.routing()

                .add(Methods.GET, "/metrics/hardware", MetricsHandler())

                .add(Methods.GET, "/metrics/files", FileHandler())
    }

}
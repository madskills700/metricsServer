package ch.madskills.metricsServer

import com.networknt.server.ShutdownHookProvider
import com.networknt.server.StartupHookProvider
import org.slf4j.LoggerFactory

/** Логгер, который применяется к обоим классам */
private val logger = LoggerFactory.getLogger("Hooks")

/** Стартовый хук */
class StartupHook : StartupHookProvider {

    override fun onStartup() {
        // StatrupHook инжектится быстрее, чем инициализируется логгер
        // это касается и shutdown хука
        println("Startup hook is injected")
    }
}

/** Хук, который отрабатывает при завершении работы приложения */
class ShutdownHook : ShutdownHookProvider {

    override fun onShutdown() {
        println("Shutdown hook started")
    }

}

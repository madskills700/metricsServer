package ch.madskills.metricsServer

import com.networknt.server.ShutdownHookProvider
import com.networknt.server.StartupHookProvider
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import java.util.*

/** Логгер, который применяется к обоим классам */
private val logger = LoggerFactory.getLogger("Hooks")

/** Стартовый хук */
class StartupHook : StartupHookProvider {

    override fun onStartup() {
        // StatrupHook инжектится быстрее, чем инициализируется логгер
        // это касается и shutdown хука
        println("Startup hook is injected")
        migrateDb()
    }

    /** Миграция БД */
    private fun migrateDb() {
        // загрузка конфигурационного файла
        // TODO брать конфиг из service.yml
        val props = Properties()
        props.load(javaClass.getResourceAsStream("/config/db.properties"))
        // инициализация flyway
        val flyway = Flyway.configure()
                .dataSource(props.getProperty("url"), props.getProperty("user"), props.getProperty("password")).load()
        // непосредственно миграция
        flyway.migrate()
    }
}

/** Хук, который отрабатывает при завершении работы приложения */
class ShutdownHook : ShutdownHookProvider {

    override fun onShutdown() {
        println("Shutdown hook started")
    }

}

package ch.madskills.metricsServer

import com.networknt.server.ShutdownHookProvider
import com.networknt.server.StartupHookProvider
import oshi.SystemInfo
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/** Стартовый хук */
class StartupHook : StartupHookProvider {

    /** Информация о системе */
    private val sysInfo = SystemInfo()

    override fun onStartup() {
        // StatrupHook инжектится быстрее, чем инициализируется логгер
        // это касается и shutdown хука
        println("Startup hook is injected")
        // инициализация конфига
        initProperties()
        // конфигурация и миграция БД
        prepareDb()
        migrateDb()
        // запуск планировщика, который складывает текущие показатели системы в БД
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(collectDataTask(sysInfo), 5, 300, TimeUnit.SECONDS)
    }
}

/** Хук, который отрабатывает при завершении работы приложения */
class ShutdownHook : ShutdownHookProvider {

    override fun onShutdown() {
        println("Shutdown hook started")
    }

}

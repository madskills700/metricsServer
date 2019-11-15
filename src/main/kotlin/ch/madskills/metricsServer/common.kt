package ch.madskills.metricsServer

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.networknt.config.Config
import com.networknt.service.SingletonServiceFactory
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import oshi.SystemInfo
import java.util.*
import javax.sql.DataSource

/** Мэппер  */
val mapper = Config.getInstance().mapper.registerModule(KotlinModule())
/** Источник данных из БД  */
val ds = SingletonServiceFactory.getBean(DataSource::class.java)
/** Логгер  */
val log = LoggerFactory.getLogger("server")
/** Проперти */
val props = Properties()

/** Подготовить коннектор к БД */
fun prepareDb() {
    props.load(object {}.javaClass.getResourceAsStream("/config/db.properties"))
    with(ds as HikariDataSource) {
        password = props.getProperty("password")
        jdbcUrl = props.getProperty("url")
        username = props.getProperty("user")
        maximumPoolSize = props.getProperty("maximumPoolSize").toInt()
    }
    Database.connect(ds)
    TransactionManager.manager.defaultRepetitionAttempts = 2
}

/** Миграция БД */
fun migrateDb() {
    // инициализация flyway
    val flyway = Flyway.configure()
            .dataSource(props.getProperty("url"), props.getProperty("user"), props.getProperty("password")).load()
    // непосредственно миграция
    flyway.migrate()
}

fun collectDataTask(sysInfo: SystemInfo): Runnable {
    return Runnable {
        val cpuTemp = sysInfo.hardware.sensors.cpuTemperature
        val cpuLoad = sysInfo.hardware.processor.getSystemLoadAverage(3)[1]
        val ramFree = sysInfo.hardware.memory.available.toDouble() / 1024 / 1024 / 1024
        val ramTotal = sysInfo.hardware.memory.total.toDouble() / 1024 / 1024 / 1024
        val totalDiskSpace = sysInfo.operatingSystem.fileSystem.fileStores[0].totalSpace.toDouble() / 1000 / 1024 / 1024
        val freeDiskSpace = sysInfo.operatingSystem.fileSystem.fileStores[0].usableSpace.toDouble() / 1000 / 1024 / 1024
        val created = System.currentTimeMillis()

        transaction {
            MetricsTable.insert {
                it[MetricsTable.cpuTemp] = cpuTemp
                it[MetricsTable.cpuLoad] = cpuLoad
                it[MetricsTable.ramFree] = ramFree
                it[MetricsTable.ramTotal] = ramTotal
                it[MetricsTable.totalDiskSpace] = totalDiskSpace
                it[MetricsTable.freeDiskSpace] = freeDiskSpace
                it[MetricsTable.created] = DateTime(created)
            }
            commit()
        }
    }
}
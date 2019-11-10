-- Создание таблицы с метриками
CREATE TABLE "system_metrics"
(
    "cpuTemp"        FLOAT     NOT NULL,
    "cpuLoad"        FLOAT     NOT NULL,
    "ramFree"        FLOAT     NOT NULL,
    "ramTotal"       FLOAT     NOT NULL,
    "totalDiskSpace" FLOAT     NOT NULL,
    "freeDiskSpace"  FLOAT     NOT NULL,
    "created"        TIMESTAMP NOT NULL
) WITH (
      OIDS= FALSE
);
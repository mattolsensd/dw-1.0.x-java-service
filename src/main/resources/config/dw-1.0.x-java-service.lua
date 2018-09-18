config.server = {
    applicationConnectors = {
        {
            type = "http",
            port = 8080
        }
    },
    adminMinThreads = 1,
    adminMaxThreads = 64,
    adminConnectors = {
        {
            type = "http",
            port = 8081
        }
    },
    registerDefaultExceptionMappers = false
}

config.jerseyClient = {
    timeout = "15s",
    connectionTimeout = "5s",
    maxThreads = 500,
    workQueueSize = 40
}

config.quartz = {
    db = {
        user = "quartz",
        password = "quartz",
        url = "jdbc:postgresql://localhost:5432/quartz",
        driverClass = "org.postgresql.Driver"
    },
    config = {
        jobStoreClass = "org.quartz.impl.jdbcjobstore.JobStoreTX",
        driverDelegateClass = "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate",
        tablePrefix = "quartz.qrtz_",
        isClustered = true,
        threadPoolClass = "org.quartz.simpl.SimpleThreadPool",
        threadCount = 2,
        instanceName = "dw-1.0.x-java-service"
    }
}

config.scheduler = {
    enabled = true,
    seconds = 3
}

config.externalExampleWorkerSeconds = 5

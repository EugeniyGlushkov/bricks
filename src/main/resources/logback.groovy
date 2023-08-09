import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

scan("60 seconds")

def logDir = System.properties['log.dir']
def logArchiveDir = "${logDir}/archive"

if ( !logDir ) {
    println "System property 'log.dir' not found. Default value will be used."
    logDir = "logs/"
}

println "log.dir: ${logDir}"

def PATTERN = "%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread][%logger{36}] %msg %exception{full}%n"

appender("console", ConsoleAppender) {
    layout(PatternLayout) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n"
    }
}

appender("mainFileAppender", RollingFileAppender) {
    file = "${logDir}/main.log"
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${logArchiveDir}/main.%d{yyyy-MM-dd}.log.zip"
        maxHistory = 15
    }
    encoder(PatternLayoutEncoder) {
        pattern = PATTERN
    }
}

logger("ru.bricks.Main", DEBUG, ["mainFileAppender", "console"], false)

root(INFO, ["console"])
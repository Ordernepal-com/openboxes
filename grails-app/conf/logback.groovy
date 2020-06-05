import grails.util.BuildSettings
import grails.util.Environment
import ch.qos.logback.classic.AsyncAppender

def HOME_DIR = "/var/log/eMarket"

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%level %logger - %msg%n"
    }
}

def currentDay = timestamp("yyyyMMdd")
appender('ROLLING', RollingFileAppender) {
    file = "${HOME_DIR}/centevent.all"
    rollingPolicy(FixedWindowRollingPolicy) {
        fileNamePattern = "${HOME_DIR}/centevent_${currentDay}.%i.log"
        minIndex = 1
        maxIndex = 9
    }
    triggeringPolicy(SizeBasedTriggeringPolicy) {
        maxFileSize = "1MB"
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%level %date %logger - %msg%n"
    }
    append = true
}
/*logger 'org.hibernate.type.descriptor.sql.BasicBinder', TRACE, ['STDOUT']
logger 'org.hibernate.SQL', TRACE, ['STDOUT']*/
def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() && targetDir != null) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%level %logger - %msg%n"
        }
    }
    logger("grails.plugins.elasticsearch", DEBUG, ['STDOUT'], false)
    logger 'grails.app.controllers.se.eventlogic.centevent.LoggingInterceptor', DEBUG, ['STDOUT'], false
    logger 'grails.app.controllers', DEBUG, ['STDOUT']
    logger 'org.pih.warehouse', DEBUG, ['STDOUT']
    logger 'grails.app.domain', DEBUG, ['STDOUT']
    logger 'grails.app.services', DEBUG, ['STDOUT']
    logger 'org.grails.jaxrs', DEBUG, ['STDOUT']
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
    logger("org.springframework.security", DEBUG, ['STDOUT'], false)
    logger("grails.plugin.springsecurity", DEBUG, ['STDOUT'], false)
    logger("org.pac4j", DEBUG, ['STDOUT'], false)
    root(ERROR, ['STDOUT', 'FULL_STACKTRACE'])
    root INFO, ['STDOUT']
} else {
    logger 'grails.app.controllers', INFO, ['ROLLING'], false
    logger 'grails.app.domain.se.eventlogic.centevent', INFO, ['ROLLING'], false
    logger 'grails.app.services.se.eventlogic.centevent', INFO, ['ROLLING'], false
    logger 'grails.app.taglib.se.eventlogic.centevent', INFO, ['ROLLING'], false
    logger 'grails.app.conf.se.eventlogic.centevent', INFO, ['ROLLING'], false
    logger 'grails.app.filters.se.eventlogic.centevent', INFO, ['ROLLING'], false
    logger 'se.eventlogic.centevent', INFO, ['ROLLING'], false
    root(ERROR, ['ROLLING'])
    root INFO, ['ROLLING']
}

appender('ASYNC', AsyncAppender) {
    appenderRef("ROLLING")
    appenderRef("STDOUT")
}

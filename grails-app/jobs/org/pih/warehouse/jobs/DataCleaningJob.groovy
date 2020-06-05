package org.pih.warehouse.jobs

import grails.core.GrailsApplication
import liquibase.util.LiquibaseUtil
import org.quartz.DisallowConcurrentExecution

@DisallowConcurrentExecution
class DataCleaningJob {

    def shipmentService
    GrailsApplication grailsApplication

    // cron job needs to be triggered after the staging deployment
    static triggers = {
        cron name: 'dataCleaningCronTrigger',
                cronExpression: grailsApplication.config.openboxes.jobs.dataCleaningJob.cronExpression
    }

    def execute(context) {

        Boolean enabled = grailsApplication.config.openboxes.jobs.dataCleaningJob.enabled
        if (!enabled) {
            return
        }

        if (LiquibaseUtil.isRunningMigrations()) {
            log.info "Postponing job execution until liquibase migrations are complete"
            return
        }

        log.debug "Starting data cleaning job at ${new Date()}"
        def startTime = System.currentTimeMillis()
        shipmentService.bulkUpdateShipments()
        log.debug "Finished data cleaning job in " + (System.currentTimeMillis() - startTime) + " ms"
    }


}

package org.pih.warehouse.jobs

import grails.core.GrailsApplication
import grails.util.Holders
import liquibase.util.LiquibaseUtil
import org.quartz.DisallowConcurrentExecution

@DisallowConcurrentExecution
class AssignIdentifierJob {

    def identifierService
    GrailsApplication grailsApplication

    static triggers = {

        String cronExpression = grailsApplication.config.openboxes.jobs.assignIdentifierJob.cronExpression
        cron name: 'assignIdentifierCronTrigger',
                cronExpression: cronExpression
    }

    def execute() {
        Boolean isEnabled = grailsApplication.config.openboxes.jobs.assignIdentifierJob.enabled
        Boolean enabled = isEnabled
        if (!enabled) {
            return
        }

        if (LiquibaseUtil.isRunningMigrations()) {
            log.info "Postponing job execution until liquibase migrations are complete"
            return
        }

        identifierService.assignProductIdentifiers()
        identifierService.assignShipmentIdentifiers()
        identifierService.assignReceiptIdentifiers()
        identifierService.assignOrderIdentifiers()
        identifierService.assignRequisitionIdentifiers()
        identifierService.assignTransactionIdentifiers()
    }


}

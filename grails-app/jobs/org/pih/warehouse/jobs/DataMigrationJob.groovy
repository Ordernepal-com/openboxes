package org.pih.warehouse.jobs

import grails.core.GrailsApplication
import grails.util.Holders
import liquibase.util.LiquibaseUtil
import org.quartz.DisallowConcurrentExecution

@DisallowConcurrentExecution
class DataMigrationJob {

    def migrationService
    GrailsApplication grailsApplication

    static triggers = {}

    def execute(context) {

        if (grailsApplication.config.openboxes.jobs.dataMigrationJob.enabled ?: false) {

            if (LiquibaseUtil.isRunningMigrations()) {
                log.info "Postponing job execution until liquibase migrations are complete"
                return
            }

            log.info "Starting data migration job at ${new Date()}"
            def startTime = System.currentTimeMillis()
            migrationService.migrateInventoryTransactions()
            log.info "Finished data migration job in " + (System.currentTimeMillis() - startTime) + " ms"
        }
    }


}

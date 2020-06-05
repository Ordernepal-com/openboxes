package org.pih.warehouse.jobs

import grails.core.GrailsApplication
import liquibase.util.LiquibaseUtil
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext

@DisallowConcurrentExecution
class UpdateExchangeRatesJob {

    def concurrent = false
    def currencyService
    GrailsApplication grailsApplication

    // cron job needs to be triggered after the staging deployment
    static triggers = {
        cron name: 'updateExchangeRatesCronTrigger',
                cronExpression: grailsApplication.config.openboxes.jobs.updateExchangeRatesJob.cronExpression
    }

    def execute(JobExecutionContext context) {

        Boolean enabled = grailsApplication.config.openboxes.jobs.updateExchangeRatesJob.enabled ?: false
        if (enabled) {

            if (LiquibaseUtil.isRunningMigrations()) {
                log.info "Postponing job execution until liquibase migrations are complete"
                return
            }

            log.info "Starting job at ${new Date()}"
            def startTime = System.currentTimeMillis()
            currencyService.updateExchangeRates()
            log.info "Finished running job in " + (System.currentTimeMillis() - startTime) + " ms"
        }
    }


}

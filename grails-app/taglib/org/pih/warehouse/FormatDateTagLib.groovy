package org.pih.warehouse


class FormatDateTagLib extends FormatTagLib {

    static namespace = "g"

    def formatDate = { attrs, body ->
        FormatTagLib formatTagLib = grailsApplication.mainContext.getBean('org.grails.plugins.web.taglib.FormatTagLib')
        if (session.timezone) {
            attrs.timeZone = session.timezone
        }
        out << formatTagLib.formatDate.call(attrs)
    }
}

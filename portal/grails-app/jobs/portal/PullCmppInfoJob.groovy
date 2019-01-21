package portal

import com.sg.pcrf.cmpp.UserNotificationRecordController


class PullCmppInfoJob {
    def userNotificationRecordService
    static triggers = {
//      simple repeatInterval: 5000l // execute job once in 5 seconds
    }

    def execute() {
        // execute job
        userNotificationRecordService.parseLog()
        println "PullCmppInfoJob execute" + new Date()
    }
}

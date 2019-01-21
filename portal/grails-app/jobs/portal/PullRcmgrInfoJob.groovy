package portal


class PullRcmgrInfoJob {
    def policyHandleService

    static triggers = {
        //simple repeatInterval: 5000l // execute job once in 5 seconds
//        simple name:'simpleTrigger', startDelay:10000, repeatInterval: repeatInterval
//        simple name:"${name}", startDelay:startDelay, repeatInterval: repeatInterval
//        cron name: 'myTrigger', cronExpression: "* */15 * * * ?"
    }



    def execute(context) {
        try {
            policyHandleService.loadAndSavePolicyData()
            log.info("pull rcmgr info job success!")
        } catch (Exception e) {
            e.printStackTrace()
        }

//        println grailsApplication.isWarDeployed()
//        println "PullRcmgrInfoJob execute" + new Date();
    }
}

package portal


class PullPCRFStatisticJob {
//    static triggers = {
//      simple repeatInterval: 5000l // execute job once in 5 seconds
//    }
def policyStatisticService
    def execute(context) {
        // execute job
       String cron =  context.mergedJobDataMap.get('cron').trim()
        policyStatisticService.exectue(cron);
    }
}

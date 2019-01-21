package portal


class PullQuotaInfoJob {
    def ossiService

    static triggers = {
//      simple repeatInterval: 5000l // execute job once in 5 seconds
    }

    def execute() {
//        println('--' * 10 + 'in pull quota info job' + '--' * 10)
        try {
            ossiService.saveSpecificQuota()
            log.info("pull rule quota info success!")
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}

package portal



class PullRuleInstallJob {
//    static triggers = {
//      simple repeatInterval: 5000l // execute job once in 5 seconds
//    }

    def ossiService

    def execute() {
        // execute job
//        println('--' * 10 + 'in pull rule install job' + '--' * 10)
        def now = new Date()
        now.set(second:0)
        try {
//            println(now.format("yyyy-MM-dd'T'HH:mm:ss'Z'"))
//            println(ossiService.getConfigProperties())
            ossiService.saveRuleInstallRecord(now, now.format("yyyy-MM-dd'T'HH:mm:ss'Z'"), now.format("yyyy-MM-dd'T'HH:mm:ss'Z'"))
            log.info("pull rule install job success!")
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}

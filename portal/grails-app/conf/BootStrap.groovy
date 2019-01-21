import org.quartz.*
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import portal.*

class BootStrap {
    def grailsApplication
    public final String CMPP_Trigger = "cmpp"
    public final String RCMGR_Trigger = "rcmgr"
    public final String OSSI_Quota_Trigger = "ossi-quota"
    public final String OSSI_Rule_Install_Trigger = "ossi-ruleInstall"
    public final String PCRF_Statistic_Trigger = "PCRF_Statistic"

//    uncomment to start schedule tasks
    def triggerList = [CMPP_Trigger, RCMGR_Trigger, OSSI_Quota_Trigger, OSSI_Rule_Install_Trigger,PCRF_Statistic_Trigger]
//    def triggerList = [PCRF_Statistic_Trigger]
    def init = { servletContext ->
        def path = grailsApplication.config.basepath
        def properties = null
        System.out.println("${this.getClass().getName()}|INFO-Launch Scheduler")
        try {
//            read config file
            def resource = new FileSystemResource("${path}${File.separator}config${File.separator}scheduler.properties")
            properties = PropertiesLoaderUtils.loadProperties(resource)
        } catch (Exception e) {
            System.err.println("${this.getClass().getName()}|EXCEP-Launch Scheduler|${e.toString()}")
            System.exit(-1);
        }
        triggerList.each() {
            def value = properties.get(it)
            if (!value) {
                System.err.println("${this.getClass().getName()}|EXCEP-Launch Scheduler|Missing Trigger configuration for ${it}")
                System.exit(-1);
            }
            def valArr = value.split("\\|", -1) // split schedule info
            def type = valArr[0]
            def validateFlag = false
            Trigger trigger = null
            if (type == "simple" && valArr.length == 3) {
                (type == "cron" && valArr.length == 2)
                validateFlag = true
                trigger = buildTrigger("simple", it, "PMITrigger", null, valArr[1], valArr[2], new Date() + 60000)
            } else if (type == "cron" && valArr.length == 2) {
                trigger = buildTrigger("cron", it, "PMITrigger", valArr[1], null, null, new Date() + 60000)
                validateFlag = true
            } else {
                System.err.println("${this.getClass().getName()}|EXCEP-Launch Scheduler|Wrong Trigger configuration ${it}=${value}")
            }
            if (validateFlag) {
                try {
                    if (CMPP_Trigger == it) {
                        PullCmppInfoJob.schedule(trigger)
                    } else if (RCMGR_Trigger == it) {
                        PullRcmgrInfoJob.schedule(trigger)
                    } else if (OSSI_Quota_Trigger == it) {
                        PullQuotaInfoJob.schedule(trigger)
                    } else if (PCRF_Statistic_Trigger == it) {
                        PullPCRFStatisticJob.schedule(valArr[1],["cron":valArr[1]])
                    } else {
                        PullRuleInstallJob.schedule(trigger)
                    }
                } catch (Exception e) {
                    System.err.println("${this.getClass().getName()}|EXCEP-Launch Scheduler|Trigger started failed ${e.toString()}")
                    System.exit(-1);
                }
            }
        }
    }


    def destroy = {
    }


    Trigger buildTrigger(
            def type, def name, def group, def cronRule, def repeatCount, def repeatInterval, def startTime) {
        println "buildTrigger:${type}|${name}|${group}|${cronRule}|${repeatCount}|${repeatInterval}|${startTime}"
        Trigger trigger = null
        if (type == "cron") {
            trigger = (CronTrigger) TriggerBuilder.newTrigger()
//                    .startAt(startTime)
                    .withIdentity(name, group)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronRule))
                    .build();
        } else {
            trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                    .withIdentity(name, group)
                    .startAt(startTime)
                    .withSchedule(SimpleScheduleBuilder
                    .simpleSchedule()
                    .withIntervalInSeconds(repeatInterval)
                    .withRepeatCount(repeatCount))
                    .build();
        }
        return trigger
    }
}

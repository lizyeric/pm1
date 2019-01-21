package com.sg.pcrf.ossi

class RuleInstallRecord {

    static constraints = {
        startTime(nullable: true)
        endTime(nullable: true)
        policyServer(nullable: true)
        isComplete(nullable: true)
        name(nullable: false)
        numberOfTimesInstalledAttemps(nullable: true)
        numberOfTimesRemovedByPCRF(nullable: true)
        numberOfTimesFailedOrRemovedByGateway(nullable: true)
        numberOfTimesTotalRetryAttempts(nullable: true)
        entryTime(nullable: true)
    }

    String startTime
    String endTime
    String policyServer
    String isComplete
    String name
    String numberOfTimesInstalledAttemps
    String numberOfTimesRemovedByPCRF
    String numberOfTimesFailedOrRemovedByGateway
    String numberOfTimesTotalRetryAttempts
    Date entryTime

    String toString() {
        "rule install record:\nST:$startTime\nET:$endTime\nPolicyServer:$policyServer\nComplete?:$isComplete\nname:$name\n" +
                "installAttempts:$numberOfTimesInstalledAttemps" +
                "removedBtPCRF:$numberOfTimesRemovedByPCRF" +
                "failedOrRemoved:$numberOfTimesFailedOrRemovedByGateway" +
                "totalRetry:$numberOfTimesTotalRetryAttempts"
    }
}

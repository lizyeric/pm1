package entity

class PolicyRecord {
    String policyCode = "0"
    String time = "0"
    String usrGrade = "0"
    String location = "0"
    String destinationAddress = "0"
    String bossUsrQuotaStatus = "0"
    String bossServiceQuotaStatus = "0"
    String pcrfQuota = "0" // forever 0
    String terminalType = "0"

    String toString(){
        "print policy record:\npolicyCode:$policyCode, time:$time, usrGrade:$usrGrade, location:$location\n" +
                "destinationAddress:$destinationAddress, bossUsrQuotaStatus:$bossUsrQuotaStatus\n" +
                "bossServiceQuotaStatus:$bossServiceQuotaStatus, pcrfQuota:$pcrfQuota, terminalType:$terminalType"
    }
}

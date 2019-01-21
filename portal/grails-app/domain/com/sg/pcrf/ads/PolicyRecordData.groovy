package com.sg.pcrf.ads

import com.sg.pcrf.cmpp.UserNotificationRecord

class PolicyRecordData {

    //PRD data pcrf_prd_tab

    static constraints = {
    }

//    static hasMany = [userNotificationRecords:UserNotificationRecord]


    String policyId

    String policyName

	String isDelete

    String refVersion
}

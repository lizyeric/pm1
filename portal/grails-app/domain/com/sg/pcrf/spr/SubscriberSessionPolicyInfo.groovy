package com.sg.pcrf.spr

class SubscriberSessionPolicyInfo {

    static constraints = {
        imsi(nullable: true)
    }
    String imsi
    String msisdn
    String policy_Code
    String notification_cycle
    String terminate_type
    Date start_time
    Date end_time
    Date operate_time
}

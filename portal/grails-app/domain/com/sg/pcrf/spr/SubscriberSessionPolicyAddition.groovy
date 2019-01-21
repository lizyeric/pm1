package com.sg.pcrf.spr

class SubscriberSessionPolicyAddition {

    static constraints = {
        imsi(nullable: true)
        msisdn(nullable: true)
        policy_Code(nullable: true)
        notification_cycle(nullable: true)
        terminate_type(nullable: true)
        start_time(nullable: true)
        end_time(nullable: true)
        operate_time(nullable: true)
    }
    String imsi
    String msisdn
    String policy_Code
    String notification_cycle
    String terminate_type
    Date start_time
    Date end_time
    Date operate_time
    String db_type
}

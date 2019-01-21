package com.sg.pcrf.spr

class SubscriberDataAddition {

    static constraints = {
        imsi(nullable: true)
        billing_Day(nullable: true)
        package_Type(nullable: true)
        usr_Billing_Type(nullable: true)
        usr_status(nullable: true)
        usr_next_reset_time(nullable: true)
    }

    String imsi
    String msisdn
    String billing_Day
    String package_Type
    Date operate_time
    String usr_Billing_Type
    int usr_status
    Date usr_next_reset_time
    String db_type
}

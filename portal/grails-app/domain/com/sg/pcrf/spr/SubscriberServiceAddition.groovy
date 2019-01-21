package com.sg.pcrf.spr

class SubscriberServiceAddition {

    static constraints = {
        imsi(nullable: true)
        msisdn(nullable: true)
        service_code(nullable: true)
        billing_type(nullable: true)
        start_date(nullable: true)
        end_date(nullable: true)
        operate_time(nullable: true)
        usage_state(nullable: true)
    }

    String imsi
    String msisdn
    String service_code
    Integer billing_type
    Date start_date
    Date end_date
    Date operate_time
    Integer usage_state
    String db_type
}

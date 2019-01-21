package com.sg.pcrf.spr

class SubscriberServiceData {

    static constraints = {
        imsi(nullable: true)
    }

    String imsi
    String msisdn
    String service_code
    Integer billing_type
    Date start_date
    Date end_date
    Date operate_time
    Integer usage_state
}

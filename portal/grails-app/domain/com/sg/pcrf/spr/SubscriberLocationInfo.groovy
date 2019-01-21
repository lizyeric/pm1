package com.sg.pcrf.spr

class SubscriberLocationInfo {

    static constraints = {
        imsi(nullable: true)
        code(nullable: true)
        code_type(nullable: true)
        usr_Location(nullable: true)
        operate_time(nullable: true)
    }

    String imsi
    String msisdn
    String code
    String code_type
    String usr_Location
    Date operate_time
}

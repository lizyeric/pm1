package com.sg.pcrf.spr

class SubscriberQuotaData {

    static constraints = {
        imsi(nullable: true)
        total_Volume(nullable: true)
    }

    String imsi
    String msisdn
    String quota_name
    String total_Volume
    Date next_ResetTime
}

package com.sg.pcrf.cmpp

import com.sg.pcrf.ads.PolicyRecordData

class UserNotificationRecord {

    //CMPP.log

    static constraints = {
        notifyContent(nullable:true)
    }

    /**
     * 通知类型，预留字段
     */
    Integer notifyType = new Integer(0)  //0.sms 1.email(reseved) 2.other(resvered)

    /**
     * 短信发送状态
     */
    Integer notifyResult = new Integer(0)  //0,success, 1:failed

    /**
     * yyyy-MM-dd hh:mm:ss 短信发送时间
     */
    String notifyDate

    /**
     * 下发短信的策略名称
     */
    String notifyPolicyName

    /**
     * 消息内容
     */
    String notifyContent

    /**
     * 接收短信的手机号码
     */
    String subscriberId
}

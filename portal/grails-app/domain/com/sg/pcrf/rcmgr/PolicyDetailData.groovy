package com.sg.pcrf.rcmgr

class PolicyDetailData {

//    PolicyDetailData(){
//        isDelete = false
//        policyCode = '0'
//        serviceCode = '0'
//    }

    static constraints = {
        policyName(unique: true, nullable: false)
        policyId(unique: true, nullable: true)
        policyAction(nullable: true, size: 2..15000)
        policyCondition(nullable: true, size: 2..15000)
        activateReason(nullable: true)
        serviceCode(nullable: true)
        policyCode(nullable: true)
    }

    static mapping = {
        policyCondition sqlType: 'text'
        policyAction sqlType: 'text'
        isDelete defaultValue: false
        serviceCode defaultValue: "'0'"
        policyCode defaultValue: "'0'"
        policyId index: 'policy_id_idx'
    }
    //策略名称
    String policyName

    //策略编号(reserved)
    String policyId

    //对应user seesion policy（默认为0）
    String policyCode = "0"

    //服务编号（默认为0）
    String serviceCode = "0"

    //策略行为
    String policyAction = ""

    //策略条件
    String policyCondition = ""

    //策略生效原因(reserved)
    String activateReason = ""

    //是否被删除标记
    boolean isDelete = false

    String toString() {
        "Name:$policyName\nId:$policyId\naction:$policyAction\ncondition:$policyCondition\nactivateReason:$activateReason\npolicyCode:$policyCode\nserviceCode:$serviceCode\nisDelete:$isDelete\n"
    }
}

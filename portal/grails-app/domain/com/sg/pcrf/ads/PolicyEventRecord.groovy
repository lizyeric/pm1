package com.sg.pcrf.ads

class PolicyEventRecord {

    static constraints = {
        adsVersion(nullable: true, size: 0..32)
        policyId(nullable: true, size: 0..32)
        refVersion(nullable: true, size: 0..32)
        policyStartTime(nullable: true)
        policyEndTime(nullable: true)
        serialNumber(nullable: true)
        sessionId(nullable: true)
        ratType(nullable: true)
        eventType(nullable: true)
        ipCanType(nullable: true)
        calledStationId(nullable: true, size: 0..32)
        mobilityProtocol(nullable: true, size: 0..32)
        userEquipInfo(nullable: true, size: 0..32)
        peerId(nullable: true)
        origHost(nullable: true, size: 0..32)
        framedIpAddr(nullable: true, size: 0..32)
        sgsnMccmnc(nullable: true, size: 0..32)
        sgsnIpaddr(nullable: true, size: 0..32)
        eventTrigger(nullable: true)
        mccmnc(nullable: true, size: 0..32)
        locationAreaCode(nullable: true, size: 0..32)
        cellId(nullable: true, size: 0..32)
        subid(nullable: true, size: 0..32)
        billDay(nullable: true)
        routingAreaCode(nullable: true, size: 0..32)
        tier(nullable: true, size: 0..32)
    }
    String policyId
    String adsVersion
    String refVersion
    Date policyStartTime
    Date policyEndTime
    Integer serialNumber
    String sessionId
    Integer ratType
    Integer eventType
    Integer ipCanType
    String calledStationId
    String mobilityProtocol
    String userEquipInfo
    String peerId
    String origHost
    String framedIpAddr
    String sgsnMccmnc
    String sgsnIpaddr
    Integer eventTrigger
    String mccmnc
    String locationAreaCode
    String cellId
    //MSISDN
    String subid
    Integer billDay
    String routingAreaCode
    String tier

}

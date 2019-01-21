package com.sg.domain.ads;

import java.io.Serializable;
import java.sql.Date;

public class PolicyEventRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 347262426070154917L;
	private String policyId;
	private String refVersion;
	private Date policyStartTime;
	private Date policyEndTime;
	private Integer serialNumber;
	private String sessionId;
	private Integer ratType;
	private Integer eventType;
	private Integer ipCanType;
	private String calledStationId;
	private String mobilityProtocol;
	private String userEquipInfo;
	private String peerId;
	private String origHost;
	private String framedIpAddr;
	private String sgsnMccmnc;
	private String sgsnIpaddr;
	private Integer eventTrigger;
	private String mccmnc;
	private String locationAreaCode; 
	private String cellId;
	private String subid;
	private Integer billDay;
	private String routingAreaCode;
	private String tier;

	public PolicyEventRecord() {
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getRefVersion() {
		return refVersion;
	}

	public void setRefVersion(String refVersion) {
		this.refVersion = refVersion;
	}

	public Date getPolicyStartTime() {
		return policyStartTime;
	}

	public void setPolicyStartTime(Date policyStartTime) {
		this.policyStartTime = policyStartTime;
	}

	public Date getPolicyEndTime() {
		return policyEndTime;
	}

	public void setPolicyEndTime(Date policyEndTime) {
		this.policyEndTime = policyEndTime;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Integer getRatType() {
		return ratType;
	}

	public void setRatType(Integer ratType) {
		this.ratType = ratType;
	}

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	public Integer getIpCanType() {
		return ipCanType;
	}

	public void setIpCanType(Integer ipCanType) {
		this.ipCanType = ipCanType;
	}

	public String getCalledStationId() {
		return calledStationId;
	}

	public void setCalledStationId(String calledStationId) {
		this.calledStationId = calledStationId;
	}

	public String getMobilityProtocol() {
		return mobilityProtocol;
	}

	public void setMobilityProtocol(String mobilityProtocol) {
		this.mobilityProtocol = mobilityProtocol;
	}

	public String getUserEquipInfo() {
		return userEquipInfo;
	}

	public void setUserEquipInfo(String userEquipInfo) {
		this.userEquipInfo = userEquipInfo;
	}

	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	public String getOrigHost() {
		return origHost;
	}

	public void setOrigHost(String origHost) {
		this.origHost = origHost;
	}

	public String getFramedIpAddr() {
		return framedIpAddr;
	}

	public void setFramedIpAddr(String framedIpAddr) {
		this.framedIpAddr = framedIpAddr;
	}

	public String getSgsnMccmnc() {
		return sgsnMccmnc;
	}

	public void setSgsnMccmnc(String sgsnMccmnc) {
		this.sgsnMccmnc = sgsnMccmnc;
	}

	public String getSgsnIpaddr() {
		return sgsnIpaddr;
	}

	public void setSgsnIpaddr(String sgsnIpaddr) {
		this.sgsnIpaddr = sgsnIpaddr;
	}

	public Integer getEventTrigger() {
		return eventTrigger;
	}

	public void setEventTrigger(Integer eventTrigger) {
		this.eventTrigger = eventTrigger;
	}

	public String getMccmnc() {
		return mccmnc;
	}

	public void setMccmnc(String mccmnc) {
		this.mccmnc = mccmnc;
	}

	public String getLocationAreaCode() {
		return locationAreaCode;
	}

	public void setLocationAreaCode(String locationAreaCode) {
		this.locationAreaCode = locationAreaCode;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getSubid() {
		return subid;
	}

	public void setSubid(String subid) {
		this.subid = subid;
	}

	public Integer getBillDay() {
		return billDay;
	}

	public void setBillDay(Integer billDay) {
		this.billDay = billDay;
	}

	public String getRoutingAreaCode() {
		return routingAreaCode;
	}

	public void setRoutingAreaCode(String routingAreaCode) {
		this.routingAreaCode = routingAreaCode;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

}
package com.sg.domain.ads;

import java.io.Serializable;

public class PolicyRecordData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7974938565705188835L;

	private String policyId;

	private String policyName;

	private String refVersion;

	public PolicyRecordData() {
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getRefVersion() {
		return refVersion;
	}

	public void setRefVersion(String refVersion) {
		this.refVersion = refVersion;
	}
}

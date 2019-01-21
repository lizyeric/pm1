<%@ page import="com.sg.pcrf.ads.PolicyEventRecord" %>



<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'billDay', 'error')} required">
	<label for="billDay">
		<g:message code="policyEventRecord.billDay.label" default="Bill Day" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="billDay" type="number" value="${policyEventRecordInstance.billDay}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'calledStationId', 'error')} required">
	<label for="calledStationId">
		<g:message code="policyEventRecord.calledStationId.label" default="Called Station Id" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="calledStationId" required="" value="${policyEventRecordInstance?.calledStationId}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'cellId', 'error')} required">
	<label for="cellId">
		<g:message code="policyEventRecord.cellId.label" default="Cell Id" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="cellId" required="" value="${policyEventRecordInstance?.cellId}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'eventTrigger', 'error')} required">
	<label for="eventTrigger">
		<g:message code="policyEventRecord.eventTrigger.label" default="Event Trigger" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="eventTrigger" type="number" value="${policyEventRecordInstance.eventTrigger}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'eventType', 'error')} required">
	<label for="eventType">
		<g:message code="policyEventRecord.eventType.label" default="Event Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="eventType" type="number" value="${policyEventRecordInstance.eventType}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'framedIpAddr', 'error')} required">
	<label for="framedIpAddr">
		<g:message code="policyEventRecord.framedIpAddr.label" default="Framed Ip Addr" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="framedIpAddr" required="" value="${policyEventRecordInstance?.framedIpAddr}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'ipCanType', 'error')} required">
	<label for="ipCanType">
		<g:message code="policyEventRecord.ipCanType.label" default="Ip Can Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="ipCanType" type="number" value="${policyEventRecordInstance.ipCanType}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'locationAreaCode', 'error')} required">
	<label for="locationAreaCode">
		<g:message code="policyEventRecord.locationAreaCode.label" default="Location Area Code" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="locationAreaCode" required="" value="${policyEventRecordInstance?.locationAreaCode}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'mccmnc', 'error')} required">
	<label for="mccmnc">
		<g:message code="policyEventRecord.mccmnc.label" default="Mccmnc" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="mccmnc" required="" value="${policyEventRecordInstance?.mccmnc}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'mobilityProtocol', 'error')} required">
	<label for="mobilityProtocol">
		<g:message code="policyEventRecord.mobilityProtocol.label" default="Mobility Protocol" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="mobilityProtocol" required="" value="${policyEventRecordInstance?.mobilityProtocol}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'origHost', 'error')} required">
	<label for="origHost">
		<g:message code="policyEventRecord.origHost.label" default="Orig Host" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="origHost" required="" value="${policyEventRecordInstance?.origHost}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'peerId', 'error')} required">
	<label for="peerId">
		<g:message code="policyEventRecord.peerId.label" default="Peer Id" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="peerId" required="" value="${policyEventRecordInstance?.peerId}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'policyEndTime', 'error')} required">
	<label for="policyEndTime">
		<g:message code="policyEventRecord.policyEndTime.label" default="Policy End Time" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="policyEndTime" precision="day"  value="${policyEventRecordInstance?.policyEndTime}"  />

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'policyId', 'error')} required">
	<label for="policyId">
		<g:message code="policyEventRecord.policyId.label" default="Policy Id" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="policyId" required="" value="${policyEventRecordInstance?.policyId}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'policyStartTime', 'error')} required">
	<label for="policyStartTime">
		<g:message code="policyEventRecord.policyStartTime.label" default="Policy Start Time" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="policyStartTime" precision="day"  value="${policyEventRecordInstance?.policyStartTime}"  />

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'ratType', 'error')} required">
	<label for="ratType">
		<g:message code="policyEventRecord.ratType.label" default="Rat Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="ratType" type="number" value="${policyEventRecordInstance.ratType}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'routingAreaCode', 'error')} required">
	<label for="routingAreaCode">
		<g:message code="policyEventRecord.routingAreaCode.label" default="Routing Area Code" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="routingAreaCode" required="" value="${policyEventRecordInstance?.routingAreaCode}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'serialNumber', 'error')} required">
	<label for="serialNumber">
		<g:message code="policyEventRecord.serialNumber.label" default="Serial Number" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="serialNumber" type="number" value="${policyEventRecordInstance.serialNumber}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'sessionId', 'error')} required">
	<label for="sessionId">
		<g:message code="policyEventRecord.sessionId.label" default="Session Id" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="sessionId" required="" value="${policyEventRecordInstance?.sessionId}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'sgsnIpaddr', 'error')} required">
	<label for="sgsnIpaddr">
		<g:message code="policyEventRecord.sgsnIpaddr.label" default="Sgsn Ipaddr" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="sgsnIpaddr" required="" value="${policyEventRecordInstance?.sgsnIpaddr}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'sgsnMccmnc', 'error')} required">
	<label for="sgsnMccmnc">
		<g:message code="policyEventRecord.sgsnMccmnc.label" default="Sgsn Mccmnc" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="sgsnMccmnc" required="" value="${policyEventRecordInstance?.sgsnMccmnc}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'subid', 'error')} required">
	<label for="subid">
		<g:message code="policyEventRecord.subid.label" default="Subid" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="subid" required="" value="${policyEventRecordInstance?.subid}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'tier', 'error')} required">
	<label for="tier">
		<g:message code="policyEventRecord.tier.label" default="Tier" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="tier" required="" value="${policyEventRecordInstance?.tier}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyEventRecordInstance, field: 'userEquipInfo', 'error')} required">
	<label for="userEquipInfo">
		<g:message code="policyEventRecord.userEquipInfo.label" default="User Equip Info" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="userEquipInfo" required="" value="${policyEventRecordInstance?.userEquipInfo}"/>

</div>


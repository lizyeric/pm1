
<%@ page import="com.sg.pcrf.ads.PolicyEventRecord" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'policyEventRecord.label', default: 'PolicyEventRecord')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-policyEventRecord" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-policyEventRecord" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list policyEventRecord">
			
				<g:if test="${policyEventRecordInstance?.billDay}">
				<li class="fieldcontain">
					<span id="billDay-label" class="property-label"><g:message code="policyEventRecord.billDay.label" default="Bill Day" /></span>
					
						<span class="property-value" aria-labelledby="billDay-label"><g:fieldValue bean="${policyEventRecordInstance}" field="billDay"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.calledStationId}">
				<li class="fieldcontain">
					<span id="calledStationId-label" class="property-label"><g:message code="policyEventRecord.calledStationId.label" default="Called Station Id" /></span>
					
						<span class="property-value" aria-labelledby="calledStationId-label"><g:fieldValue bean="${policyEventRecordInstance}" field="calledStationId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.cellId}">
				<li class="fieldcontain">
					<span id="cellId-label" class="property-label"><g:message code="policyEventRecord.cellId.label" default="Cell Id" /></span>
					
						<span class="property-value" aria-labelledby="cellId-label"><g:fieldValue bean="${policyEventRecordInstance}" field="cellId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.eventTrigger}">
				<li class="fieldcontain">
					<span id="eventTrigger-label" class="property-label"><g:message code="policyEventRecord.eventTrigger.label" default="Event Trigger" /></span>
					
						<span class="property-value" aria-labelledby="eventTrigger-label"><g:fieldValue bean="${policyEventRecordInstance}" field="eventTrigger"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.eventType}">
				<li class="fieldcontain">
					<span id="eventType-label" class="property-label"><g:message code="policyEventRecord.eventType.label" default="Event Type" /></span>
					
						<span class="property-value" aria-labelledby="eventType-label"><g:fieldValue bean="${policyEventRecordInstance}" field="eventType"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.framedIpAddr}">
				<li class="fieldcontain">
					<span id="framedIpAddr-label" class="property-label"><g:message code="policyEventRecord.framedIpAddr.label" default="Framed Ip Addr" /></span>
					
						<span class="property-value" aria-labelledby="framedIpAddr-label"><g:fieldValue bean="${policyEventRecordInstance}" field="framedIpAddr"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.ipCanType}">
				<li class="fieldcontain">
					<span id="ipCanType-label" class="property-label"><g:message code="policyEventRecord.ipCanType.label" default="Ip Can Type" /></span>
					
						<span class="property-value" aria-labelledby="ipCanType-label"><g:fieldValue bean="${policyEventRecordInstance}" field="ipCanType"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.locationAreaCode}">
				<li class="fieldcontain">
					<span id="locationAreaCode-label" class="property-label"><g:message code="policyEventRecord.locationAreaCode.label" default="Location Area Code" /></span>
					
						<span class="property-value" aria-labelledby="locationAreaCode-label"><g:fieldValue bean="${policyEventRecordInstance}" field="locationAreaCode"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.mccmnc}">
				<li class="fieldcontain">
					<span id="mccmnc-label" class="property-label"><g:message code="policyEventRecord.mccmnc.label" default="Mccmnc" /></span>
					
						<span class="property-value" aria-labelledby="mccmnc-label"><g:fieldValue bean="${policyEventRecordInstance}" field="mccmnc"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.mobilityProtocol}">
				<li class="fieldcontain">
					<span id="mobilityProtocol-label" class="property-label"><g:message code="policyEventRecord.mobilityProtocol.label" default="Mobility Protocol" /></span>
					
						<span class="property-value" aria-labelledby="mobilityProtocol-label"><g:fieldValue bean="${policyEventRecordInstance}" field="mobilityProtocol"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.origHost}">
				<li class="fieldcontain">
					<span id="origHost-label" class="property-label"><g:message code="policyEventRecord.origHost.label" default="Orig Host" /></span>
					
						<span class="property-value" aria-labelledby="origHost-label"><g:fieldValue bean="${policyEventRecordInstance}" field="origHost"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.peerId}">
				<li class="fieldcontain">
					<span id="peerId-label" class="property-label"><g:message code="policyEventRecord.peerId.label" default="Peer Id" /></span>
					
						<span class="property-value" aria-labelledby="peerId-label"><g:fieldValue bean="${policyEventRecordInstance}" field="peerId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.policyEndTime}">
				<li class="fieldcontain">
					<span id="policyEndTime-label" class="property-label"><g:message code="policyEventRecord.policyEndTime.label" default="Policy End Time" /></span>
					
						<span class="property-value" aria-labelledby="policyEndTime-label"><g:formatDate date="${policyEventRecordInstance?.policyEndTime}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.policyId}">
				<li class="fieldcontain">
					<span id="policyId-label" class="property-label"><g:message code="policyEventRecord.policyId.label" default="Policy Id" /></span>
					
						<span class="property-value" aria-labelledby="policyId-label"><g:fieldValue bean="${policyEventRecordInstance}" field="policyId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.policyStartTime}">
				<li class="fieldcontain">
					<span id="policyStartTime-label" class="property-label"><g:message code="policyEventRecord.policyStartTime.label" default="Policy Start Time" /></span>
					
						<span class="property-value" aria-labelledby="policyStartTime-label"><g:formatDate date="${policyEventRecordInstance?.policyStartTime}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.ratType}">
				<li class="fieldcontain">
					<span id="ratType-label" class="property-label"><g:message code="policyEventRecord.ratType.label" default="Rat Type" /></span>
					
						<span class="property-value" aria-labelledby="ratType-label"><g:fieldValue bean="${policyEventRecordInstance}" field="ratType"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.routingAreaCode}">
				<li class="fieldcontain">
					<span id="routingAreaCode-label" class="property-label"><g:message code="policyEventRecord.routingAreaCode.label" default="Routing Area Code" /></span>
					
						<span class="property-value" aria-labelledby="routingAreaCode-label"><g:fieldValue bean="${policyEventRecordInstance}" field="routingAreaCode"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.serialNumber}">
				<li class="fieldcontain">
					<span id="serialNumber-label" class="property-label"><g:message code="policyEventRecord.serialNumber.label" default="Serial Number" /></span>
					
						<span class="property-value" aria-labelledby="serialNumber-label"><g:fieldValue bean="${policyEventRecordInstance}" field="serialNumber"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.sessionId}">
				<li class="fieldcontain">
					<span id="sessionId-label" class="property-label"><g:message code="policyEventRecord.sessionId.label" default="Session Id" /></span>
					
						<span class="property-value" aria-labelledby="sessionId-label"><g:fieldValue bean="${policyEventRecordInstance}" field="sessionId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.sgsnIpaddr}">
				<li class="fieldcontain">
					<span id="sgsnIpaddr-label" class="property-label"><g:message code="policyEventRecord.sgsnIpaddr.label" default="Sgsn Ipaddr" /></span>
					
						<span class="property-value" aria-labelledby="sgsnIpaddr-label"><g:fieldValue bean="${policyEventRecordInstance}" field="sgsnIpaddr"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.sgsnMccmnc}">
				<li class="fieldcontain">
					<span id="sgsnMccmnc-label" class="property-label"><g:message code="policyEventRecord.sgsnMccmnc.label" default="Sgsn Mccmnc" /></span>
					
						<span class="property-value" aria-labelledby="sgsnMccmnc-label"><g:fieldValue bean="${policyEventRecordInstance}" field="sgsnMccmnc"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.subid}">
				<li class="fieldcontain">
					<span id="subid-label" class="property-label"><g:message code="policyEventRecord.subid.label" default="Subid" /></span>
					
						<span class="property-value" aria-labelledby="subid-label"><g:fieldValue bean="${policyEventRecordInstance}" field="subid"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.tier}">
				<li class="fieldcontain">
					<span id="tier-label" class="property-label"><g:message code="policyEventRecord.tier.label" default="Tier" /></span>
					
						<span class="property-value" aria-labelledby="tier-label"><g:fieldValue bean="${policyEventRecordInstance}" field="tier"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyEventRecordInstance?.userEquipInfo}">
				<li class="fieldcontain">
					<span id="userEquipInfo-label" class="property-label"><g:message code="policyEventRecord.userEquipInfo.label" default="User Equip Info" /></span>
					
						<span class="property-value" aria-labelledby="userEquipInfo-label"><g:fieldValue bean="${policyEventRecordInstance}" field="userEquipInfo"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:policyEventRecordInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${policyEventRecordInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>


<%@ page import="com.sg.pcrf.ads.PolicyEventRecord" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'policyEventRecord.label', default: 'PolicyEventRecord')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-policyEventRecord" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-policyEventRecord" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="billDay" title="${message(code: 'policyEventRecord.billDay.label', default: 'Bill Day')}" />
					
						<g:sortableColumn property="calledStationId" title="${message(code: 'policyEventRecord.calledStationId.label', default: 'Called Station Id')}" />
					
						<g:sortableColumn property="cellId" title="${message(code: 'policyEventRecord.cellId.label', default: 'Cell Id')}" />
					
						<g:sortableColumn property="eventTrigger" title="${message(code: 'policyEventRecord.eventTrigger.label', default: 'Event Trigger')}" />
					
						<g:sortableColumn property="eventType" title="${message(code: 'policyEventRecord.eventType.label', default: 'Event Type')}" />
					
						<g:sortableColumn property="framedIpAddr" title="${message(code: 'policyEventRecord.framedIpAddr.label', default: 'Framed Ip Addr')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${policyEventRecordInstanceList}" status="i" var="policyEventRecordInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${policyEventRecordInstance.id}">${fieldValue(bean: policyEventRecordInstance, field: "billDay")}</g:link></td>
					
						<td>${fieldValue(bean: policyEventRecordInstance, field: "calledStationId")}</td>
					
						<td>${fieldValue(bean: policyEventRecordInstance, field: "cellId")}</td>
					
						<td>${fieldValue(bean: policyEventRecordInstance, field: "eventTrigger")}</td>
					
						<td>${fieldValue(bean: policyEventRecordInstance, field: "eventType")}</td>
					
						<td>${fieldValue(bean: policyEventRecordInstance, field: "framedIpAddr")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${policyEventRecordInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>

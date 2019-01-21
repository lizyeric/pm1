
<%@ page import="com.sg.pcrf.cmpp.UserNotificationRecord" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'userNotificationRecord.label', default: 'UserNotificationRecord')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-userNotificationRecord" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-userNotificationRecord" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="notifyContent" title="${message(code: 'userNotificationRecord.notifyContent.label', default: 'Notify Content')}" />
					
						<g:sortableColumn property="notifyDate" title="${message(code: 'userNotificationRecord.notifyDate.label', default: 'Notify Date')}" />
					
						<g:sortableColumn property="notifyPolicyName" title="${message(code: 'userNotificationRecord.notifyPolicyName.label', default: 'Notify Policy Name')}" />
					
						<g:sortableColumn property="notifyResult" title="${message(code: 'userNotificationRecord.notifyResult.label', default: 'Notify Result')}" />
					
						<g:sortableColumn property="notifyType" title="${message(code: 'userNotificationRecord.notifyType.label', default: 'Notify Type')}" />
					
						<g:sortableColumn property="subscriberId" title="${message(code: 'userNotificationRecord.subscriberId.label', default: 'Subscriber Id')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${userNotificationRecordInstanceList}" status="i" var="userNotificationRecordInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${userNotificationRecordInstance.id}">${fieldValue(bean: userNotificationRecordInstance, field: "notifyContent")}</g:link></td>
					
						<td>${fieldValue(bean: userNotificationRecordInstance, field: "notifyDate")}</td>
					
						<td>${fieldValue(bean: userNotificationRecordInstance, field: "notifyPolicyName")}</td>
					
						<td>${fieldValue(bean: userNotificationRecordInstance, field: "notifyResult")}</td>
					
						<td>${fieldValue(bean: userNotificationRecordInstance, field: "notifyType")}</td>
					
						<td>${fieldValue(bean: userNotificationRecordInstance, field: "subscriberId")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${userNotificationRecordInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>

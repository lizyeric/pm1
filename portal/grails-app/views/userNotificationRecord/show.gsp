
<%@ page import="com.sg.pcrf.cmpp.UserNotificationRecord" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'userNotificationRecord.label', default: 'UserNotificationRecord')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-userNotificationRecord" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-userNotificationRecord" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list userNotificationRecord">
			
				<g:if test="${userNotificationRecordInstance?.notifyContent}">
				<li class="fieldcontain">
					<span id="notifyContent-label" class="property-label"><g:message code="userNotificationRecord.notifyContent.label" default="Notify Content" /></span>
					
						<span class="property-value" aria-labelledby="notifyContent-label"><g:fieldValue bean="${userNotificationRecordInstance}" field="notifyContent"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${userNotificationRecordInstance?.notifyDate}">
				<li class="fieldcontain">
					<span id="notifyDate-label" class="property-label"><g:message code="userNotificationRecord.notifyDate.label" default="Notify Date" /></span>
					
						<span class="property-value" aria-labelledby="notifyDate-label"><g:fieldValue bean="${userNotificationRecordInstance}" field="notifyDate"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${userNotificationRecordInstance?.notifyPolicyName}">
				<li class="fieldcontain">
					<span id="notifyPolicyName-label" class="property-label"><g:message code="userNotificationRecord.notifyPolicyName.label" default="Notify Policy Name" /></span>
					
						<span class="property-value" aria-labelledby="notifyPolicyName-label"><g:fieldValue bean="${userNotificationRecordInstance}" field="notifyPolicyName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${userNotificationRecordInstance?.notifyResult}">
				<li class="fieldcontain">
					<span id="notifyResult-label" class="property-label"><g:message code="userNotificationRecord.notifyResult.label" default="Notify Result" /></span>
					
						<span class="property-value" aria-labelledby="notifyResult-label"><g:fieldValue bean="${userNotificationRecordInstance}" field="notifyResult"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${userNotificationRecordInstance?.notifyType}">
				<li class="fieldcontain">
					<span id="notifyType-label" class="property-label"><g:message code="userNotificationRecord.notifyType.label" default="Notify Type" /></span>
					
						<span class="property-value" aria-labelledby="notifyType-label"><g:fieldValue bean="${userNotificationRecordInstance}" field="notifyType"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${userNotificationRecordInstance?.subscriberId}">
				<li class="fieldcontain">
					<span id="subscriberId-label" class="property-label"><g:message code="userNotificationRecord.subscriberId.label" default="Subscriber Id" /></span>
					
						<span class="property-value" aria-labelledby="subscriberId-label"><g:fieldValue bean="${userNotificationRecordInstance}" field="subscriberId"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:userNotificationRecordInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${userNotificationRecordInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

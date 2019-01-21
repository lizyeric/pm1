
<%@ page import="com.sg.pcrf.spr.ServiceSubscriberRecord" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'serviceSubscriberRecord.label', default: 'ServiceSubscriberRecord')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-serviceSubscriberRecord" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-serviceSubscriberRecord" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list serviceSubscriberRecord">
			
				<g:if test="${serviceSubscriberRecordInstance?.serviceCode}">
				<li class="fieldcontain">
					<span id="serviceCode-label" class="property-label"><g:message code="serviceSubscriberRecord.serviceCode.label" default="Service Code" /></span>
					
						<span class="property-value" aria-labelledby="serviceCode-label"><g:fieldValue bean="${serviceSubscriberRecordInstance}" field="serviceCode"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${serviceSubscriberRecordInstance?.subscriberNo}">
				<li class="fieldcontain">
					<span id="subscriberNo-label" class="property-label"><g:message code="serviceSubscriberRecord.subscriberNo.label" default="Subscriber No" /></span>
					
						<span class="property-value" aria-labelledby="subscriberNo-label"><g:fieldValue bean="${serviceSubscriberRecordInstance}" field="subscriberNo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${serviceSubscriberRecordInstance?.timeType}">
				<li class="fieldcontain">
					<span id="timeType-label" class="property-label"><g:message code="serviceSubscriberRecord.timeType.label" default="Time Type" /></span>
					
						<span class="property-value" aria-labelledby="timeType-label"><g:fieldValue bean="${serviceSubscriberRecordInstance}" field="timeType"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${serviceSubscriberRecordInstance?.timeVal}">
				<li class="fieldcontain">
					<span id="timeVal-label" class="property-label"><g:message code="serviceSubscriberRecord.timeVal.label" default="Time Val" /></span>
					
						<span class="property-value" aria-labelledby="timeVal-label"><g:fieldValue bean="${serviceSubscriberRecordInstance}" field="timeVal"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:serviceSubscriberRecordInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${serviceSubscriberRecordInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

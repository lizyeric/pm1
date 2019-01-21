
<%@ page import="com.sg.pcrf.ads.PolicyRecordData" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'policyRecordData.label', default: 'PolicyRecordData')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-policyRecordData" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-policyRecordData" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list policyRecordData">
			
				<g:if test="${policyRecordDataInstance?.policyId}">
				<li class="fieldcontain">
					<span id="policyId-label" class="property-label"><g:message code="policyRecordData.policyId.label" default="Policy Id" /></span>
					
						<span class="property-value" aria-labelledby="policyId-label"><g:fieldValue bean="${policyRecordDataInstance}" field="policyId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyRecordDataInstance?.policyName}">
				<li class="fieldcontain">
					<span id="policyName-label" class="property-label"><g:message code="policyRecordData.policyName.label" default="Policy Name" /></span>
					
						<span class="property-value" aria-labelledby="policyName-label"><g:fieldValue bean="${policyRecordDataInstance}" field="policyName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyRecordDataInstance?.refVersion}">
				<li class="fieldcontain">
					<span id="refVersion-label" class="property-label"><g:message code="policyRecordData.refVersion.label" default="Ref Version" /></span>
					
						<span class="property-value" aria-labelledby="refVersion-label"><g:fieldValue bean="${policyRecordDataInstance}" field="refVersion"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:policyRecordDataInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${policyRecordDataInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

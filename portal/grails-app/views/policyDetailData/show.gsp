
<%@ page import="com.sg.pcrf.rcmgr.PolicyDetailData" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'policyDetailData.label', default: 'PolicyDetailData')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-policyDetailData" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-policyDetailData" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list policyDetailData">
			
				<g:if test="${policyDetailDataInstance?.policyName}">
				<li class="fieldcontain">
					<span id="policyName-label" class="property-label"><g:message code="policyDetailData.policyName.label" default="Policy Name" /></span>
					
						<span class="property-value" aria-labelledby="policyName-label"><g:fieldValue bean="${policyDetailDataInstance}" field="policyName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyDetailDataInstance?.policyId}">
				<li class="fieldcontain">
					<span id="policyId-label" class="property-label"><g:message code="policyDetailData.policyId.label" default="Policy Id" /></span>
					
						<span class="property-value" aria-labelledby="policyId-label"><g:fieldValue bean="${policyDetailDataInstance}" field="policyId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyDetailDataInstance?.policyAction}">
				<li class="fieldcontain">
					<span id="policyAction-label" class="property-label"><g:message code="policyDetailData.policyAction.label" default="Policy Action" /></span>
					
						<span class="property-value" aria-labelledby="policyAction-label"><g:fieldValue bean="${policyDetailDataInstance}" field="policyAction"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyDetailDataInstance?.policyCondition}">
				<li class="fieldcontain">
					<span id="policyCondition-label" class="property-label"><g:message code="policyDetailData.policyCondition.label" default="Policy Condition" /></span>
					
						<span class="property-value" aria-labelledby="policyCondition-label"><g:fieldValue bean="${policyDetailDataInstance}" field="policyCondition"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${policyDetailDataInstance?.activateReason}">
				<li class="fieldcontain">
					<span id="activateReason-label" class="property-label"><g:message code="policyDetailData.activateReason.label" default="Activate Reason" /></span>
					
						<span class="property-value" aria-labelledby="activateReason-label"><g:fieldValue bean="${policyDetailDataInstance}" field="activateReason"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:policyDetailDataInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${policyDetailDataInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

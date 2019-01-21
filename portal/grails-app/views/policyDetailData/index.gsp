
<%@ page import="com.sg.pcrf.rcmgr.PolicyDetailData" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'policyDetailData.label', default: 'PolicyDetailData')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-policyDetailData" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-policyDetailData" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="policyName" title="${message(code: 'policyDetailData.policyName.label', default: 'Policy Name')}" />
					
						<g:sortableColumn property="policyId" title="${message(code: 'policyDetailData.policyId.label', default: 'Policy Id')}" />
					
						<g:sortableColumn property="policyAction" title="${message(code: 'policyDetailData.policyAction.label', default: 'Policy Action')}" />
					
						<g:sortableColumn property="policyCondition" title="${message(code: 'policyDetailData.policyCondition.label', default: 'Policy Condition')}" />
					
						<g:sortableColumn property="activateReason" title="${message(code: 'policyDetailData.activateReason.label', default: 'Activate Reason')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${policyDetailDataInstanceList}" status="i" var="policyDetailDataInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${policyDetailDataInstance.id}">${fieldValue(bean: policyDetailDataInstance, field: "policyName")}</g:link></td>
					
						<td>${fieldValue(bean: policyDetailDataInstance, field: "policyId")}</td>
					
						<td>${fieldValue(bean: policyDetailDataInstance, field: "policyAction")}</td>
					
						<td>${fieldValue(bean: policyDetailDataInstance, field: "policyCondition")}</td>
					
						<td>${fieldValue(bean: policyDetailDataInstance, field: "activateReason")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${policyDetailDataInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>


<%@ page import="com.sg.pcrf.ads.PolicyRecordData" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'policyRecordData.label', default: 'PolicyRecordData')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-policyRecordData" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-policyRecordData" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="policyId" title="${message(code: 'policyRecordData.policyId.label', default: 'Policy Id')}" />
					
						<g:sortableColumn property="policyName" title="${message(code: 'policyRecordData.policyName.label', default: 'Policy Name')}" />
					
						<g:sortableColumn property="refVersion" title="${message(code: 'policyRecordData.refVersion.label', default: 'Ref Version')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${policyRecordDataInstanceList}" status="i" var="policyRecordDataInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${policyRecordDataInstance.id}">${fieldValue(bean: policyRecordDataInstance, field: "policyId")}</g:link></td>
					
						<td>${fieldValue(bean: policyRecordDataInstance, field: "policyName")}</td>
					
						<td>${fieldValue(bean: policyRecordDataInstance, field: "refVersion")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${policyRecordDataInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>

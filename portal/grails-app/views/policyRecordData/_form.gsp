<%@ page import="com.sg.pcrf.ads.PolicyRecordData" %>



<div class="fieldcontain ${hasErrors(bean: policyRecordDataInstance, field: 'policyId', 'error')} required">
	<label for="policyId">
		<g:message code="policyRecordData.policyId.label" default="Policy Id" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="policyId" required="" value="${policyRecordDataInstance?.policyId}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyRecordDataInstance, field: 'policyName', 'error')} required">
	<label for="policyName">
		<g:message code="policyRecordData.policyName.label" default="Policy Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="policyName" required="" value="${policyRecordDataInstance?.policyName}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyRecordDataInstance, field: 'refVersion', 'error')} required">
	<label for="refVersion">
		<g:message code="policyRecordData.refVersion.label" default="Ref Version" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="refVersion" required="" value="${policyRecordDataInstance?.refVersion}"/>

</div>


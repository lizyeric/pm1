<%@ page import="com.sg.pcrf.rcmgr.PolicyDetailData" %>



<div class="fieldcontain ${hasErrors(bean: policyDetailDataInstance, field: 'policyName', 'error')} required">
	<label for="policyName">
		<g:message code="policyDetailData.policyName.label" default="Policy Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="policyName" required="" value="${policyDetailDataInstance?.policyName}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyDetailDataInstance, field: 'policyId', 'error')} ">
	<label for="policyId">
		<g:message code="policyDetailData.policyId.label" default="Policy Id" />
		
	</label>
	<g:textField name="policyId" value="${policyDetailDataInstance?.policyId}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyDetailDataInstance, field: 'policyAction', 'error')} ">
	<label for="policyAction">
		<g:message code="policyDetailData.policyAction.label" default="Policy Action" />
		
	</label>
	<g:textField name="policyAction" value="${policyDetailDataInstance?.policyAction}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyDetailDataInstance, field: 'policyCondition', 'error')} ">
	<label for="policyCondition">
		<g:message code="policyDetailData.policyCondition.label" default="Policy Condition" />
		
	</label>
	<g:textField name="policyCondition" value="${policyDetailDataInstance?.policyCondition}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: policyDetailDataInstance, field: 'activateReason', 'error')} ">
	<label for="activateReason">
		<g:message code="policyDetailData.activateReason.label" default="Activate Reason" />
		
	</label>
	<g:textField name="activateReason" value="${policyDetailDataInstance?.activateReason}"/>

</div>


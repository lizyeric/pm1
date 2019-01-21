<%@ page import="com.sg.pcrf.cmpp.UserNotificationRecord" %>



<div class="fieldcontain ${hasErrors(bean: userNotificationRecordInstance, field: 'notifyContent', 'error')} ">
	<label for="notifyContent">
		<g:message code="userNotificationRecord.notifyContent.label" default="Notify Content" />
		
	</label>
	<g:textField name="notifyContent" value="${userNotificationRecordInstance?.notifyContent}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userNotificationRecordInstance, field: 'notifyDate', 'error')} required">
	<label for="notifyDate">
		<g:message code="userNotificationRecord.notifyDate.label" default="Notify Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="notifyDate" required="" value="${userNotificationRecordInstance?.notifyDate}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userNotificationRecordInstance, field: 'notifyPolicyName', 'error')} required">
	<label for="notifyPolicyName">
		<g:message code="userNotificationRecord.notifyPolicyName.label" default="Notify Policy Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="notifyPolicyName" required="" value="${userNotificationRecordInstance?.notifyPolicyName}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userNotificationRecordInstance, field: 'notifyResult', 'error')} required">
	<label for="notifyResult">
		<g:message code="userNotificationRecord.notifyResult.label" default="Notify Result" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="notifyResult" type="number" value="${userNotificationRecordInstance.notifyResult}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: userNotificationRecordInstance, field: 'notifyType', 'error')} required">
	<label for="notifyType">
		<g:message code="userNotificationRecord.notifyType.label" default="Notify Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="notifyType" type="number" value="${userNotificationRecordInstance.notifyType}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: userNotificationRecordInstance, field: 'subscriberId', 'error')} required">
	<label for="subscriberId">
		<g:message code="userNotificationRecord.subscriberId.label" default="Subscriber Id" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="subscriberId" required="" value="${userNotificationRecordInstance?.subscriberId}"/>

</div>


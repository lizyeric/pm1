<%@ page import="com.sg.pcrf.spr.ServiceSubscriberRecord" %>



<div class="fieldcontain ${hasErrors(bean: serviceSubscriberRecordInstance, field: 'serviceCode', 'error')} required">
	<label for="serviceCode">
		<g:message code="serviceSubscriberRecord.serviceCode.label" default="Service Code" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="serviceCode" required="" value="${serviceSubscriberRecordInstance?.serviceCode}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: serviceSubscriberRecordInstance, field: 'subscriberNo', 'error')} required">
	<label for="subscriberNo">
		<g:message code="serviceSubscriberRecord.subscriberNo.label" default="Subscriber No" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="subscriberNo" required="" value="${serviceSubscriberRecordInstance?.subscriberNo}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: serviceSubscriberRecordInstance, field: 'timeType', 'error')} required">
	<label for="timeType">
		<g:message code="serviceSubscriberRecord.timeType.label" default="Time Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="timeType" required="" value="${serviceSubscriberRecordInstance?.timeType}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: serviceSubscriberRecordInstance, field: 'timeVal', 'error')} required">
	<label for="timeVal">
		<g:message code="serviceSubscriberRecord.timeVal.label" default="Time Val" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="timeVal" required="" value="${serviceSubscriberRecordInstance?.timeVal}"/>

</div>


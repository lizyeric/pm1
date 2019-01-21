<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>For jyx test</title>
    <asset:stylesheet src="semantic.min.css"/>
    %{--<asset:javascript src="semantic.min.js"/>--}%
</head>

<body>
    <h3 class="ui center aligned header">
        超级简单的测试页面哇！
    </h3>
    <div class="ui center aligned vertical segment">
        <g:form class="ui form" controller="testPage" action="invoke_jyx_func">
            <input style="width: 40%;" type="hidden" name="invoke" value="rcmgr">
            <button class="ui inverted blue button" type="submit">policyDetailData</button>
        </g:form>

        <g:form class="ui form" controller="testPage" action="invoke_jyx_func">
            <div class="field">
                <lable><b>Start Time</b></lable><br/>
                <input style="width: 40%;" type="text" name="startTime">
            </div>

            <div class="field">
                <lable><b>End Time</b></lable><br/>
                <input style="width: 40%;" type="text" name="endTime">
            </div>

            <input style="width: 40%;" type="hidden" name="invoke" value="rir">
            <button class="ui inverted orange button" type="submit">ruleInstallRecord</button>
        </g:form>

        <g:form class="ui form" controller="testPage" action="invoke_jyx_func">
            <input style="width: 40%;" type="hidden" name="invoke" value="sq">
            <button class="ui inverted red button" type="submit">specificQuota</button>
        </g:form>
    </div>
</body>
</html>
<%@ page import="codes.recursive.blog.Post" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
</head>
<body>
    <h1>Contact</h1>

    <div class="pad-bottom-10">
        <g:if test="${flash.message}">
            <ui:successContainer>${flash.message}</ui:successContainer>
        </g:if>

        <g:if test="${flash.error}">
            <ui:errorContainer>${flash.error}</ui:errorContainer>
        </g:if>
    </div>

    <g:form useToken="true" action="${actionName}" method="POST" class="form-horizontal" params="${defaultParams}">

        <bootform:horizontalField field="name" required="true" labelColumnClass="col-sm-2" label="Name" bean="${command}" description="Your name">
            <g:textField type="text" id="name" name="name" class="form-control" required="true" maxlength="100" value="${command.name}"/>
        </bootform:horizontalField>

        <bootform:horizontalField field="email" required="true" labelColumnClass="col-sm-2" label="Email" bean="${command}" description="Your email address">
            <g:textField type="text" id="email" name="email" class="form-control" required="true" maxlength="250" value="${command.email}"/>
        </bootform:horizontalField>

        <bootform:horizontalField field="comments" required="true" labelColumnClass="col-sm-2" label="Comments" bean="${command}" description="Your comments">
            <g:textArea id="comments" name="comments" rows="10" class="form-control" required="true" maxlength="4000" value="${command.comments}"/>
        </bootform:horizontalField>

        <div class="fieldcontainer form-group required">
        	<label class="col-sm-2 control-label" for="contactSubmit">&nbsp;</label>
        	<div class="col-sm-4">
                <input type="submit" value="Send" id="contactSubmit" class="btn btn-primary btn-sm" />
        	</div>
        </div>

    </g:form>
</body>
</html>

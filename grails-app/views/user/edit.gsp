<%--
  Created by IntelliJ IDEA.
  User: 558848
  Date: 3/7/2016
  Time: 11:08 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="admin"/>
    <title>Edit User</title>
</head>

<body>

    <h1>${params.id ? 'Edit' : 'Create'} User</h1>

    <div class="pad-bottom-10">
        <g:link controller="user" action="list" class="btn btn-primary"><i class="fa fa-angle-double-left"></i> List Users</g:link>
    </div>

    <div class="pad-bottom-10">
        <g:if test="${flash.message}">
            <ui:successContainer>${flash.message}</ui:successContainer>
        </g:if>

        <g:if test="${flash.error}">
            <ui:errorContainer>${flash.error}</ui:errorContainer>
        </g:if>
    </div>

    <g:form useToken="true" action="${actionName}" method="POST" class="form-horizontal" params="${defaultParams}">
        <g:hiddenField name="version" value="${command.version}"/>

        <bootform:horizontalField field="firstName" required="true" labelColumnClass="col-sm-2" controlColumnClass="col-sm-4" label="First Name" bean="${command}" description="">
            <g:textField type="text" id="firstName" name="firstName" class="form-control" required="true" maxlength="100" value="${command.firstName}"/>
        </bootform:horizontalField>

        <bootform:horizontalField field="lastName" required="true" labelColumnClass="col-sm-2" controlColumnClass="col-sm-4" label="Last Name" bean="${command}" description="">
            <g:textField type="text" id="lastName" name="lastName" class="form-control" required="true" maxlength="100" value="${command.lastName}"/>
        </bootform:horizontalField>

        <bootform:horizontalField field="username" required="true" labelColumnClass="col-sm-2" controlColumnClass="col-sm-4" label="Username" bean="${command}" description="">
            <g:textField type="text" id="username" name="username" class="form-control" required="true" maxlength="100" value="${command.username}"/>
        </bootform:horizontalField>

        <bootform:horizontalField field="password" required="true" labelColumnClass="col-sm-2" controlColumnClass="col-sm-4" label="Password" bean="${command}" description="">
            <g:passwordField id="password" name="password" class="form-control" required="true" maxlength="100" value=""/>
        </bootform:horizontalField>

        <ui:messageContainer>
            <div class="text-center">
                <g:submitButton id="btnSubmit" name="btnSubmit" class="btn btn-primary" value="Save"/>
                <g:link action="list" class="btn btn-default">Cancel</g:link>
            </div>
        </ui:messageContainer>

    </g:form>

</body>
</html>
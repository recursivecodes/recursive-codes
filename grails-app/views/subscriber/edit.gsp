<%--
  Created by IntelliJ IDEA.
  Subscriber: 558848
  Date: 2/16/2020
  Time: 10:10 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="admin"/>
    <title>Edit Subscriber</title>
</head>

<body>

<h1>${params.id ? 'Edit' : 'Create'} Subscriber</h1>

<div class="pad-bottom-10">
    <g:link controller="subscriber" action="list" class="btn btn-primary"><i class="fa fa-angle-double-left"></i> List Subscribers</g:link>
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
    <g:hiddenField name="version" value="${subscriber.version}"/>

    <bootform:horizontalField field="email" required="true" labelColumnClass="col-sm-2" controlColumnClass="col-sm-4" label="Email" bean="${subscriber}" description="">
        <g:textField type="text" id="email" name="email" class="form-control" required="true" maxlength="100" value="${subscriber.email}"/>
    </bootform:horizontalField>

    <bootform:horizontalField field="isActive" required="false" labelColumnClass="col-sm-2" controlColumnClass="col-sm-4" label="Active?" bean="${subscriber}" description="">
        <g:checkBox id="isActive" name="isActive" class="form-check-input" checked="${subscriber.isActive}" value="true" />
    </bootform:horizontalField>

    <bootform:horizontalField field="verificationToken" required="false" labelColumnClass="col-sm-2" controlColumnClass="col-sm-4" label="Verification Token" bean="${subscriber}" description="">
        <g:textField type="text" id="verificationToken" name="verificationToken" class="form-control" required="false" maxlength="100" value="${subscriber.verificationToken}"/>
    </bootform:horizontalField>

    <bootform:horizontalField field="isVerified" required="false" labelColumnClass="col-sm-2" controlColumnClass="col-sm-4" label="Verified?" bean="${subscriber}" description="">
        <g:checkBox id="isVerified" name="isVerified" class="form-check-input" checked="${subscriber.isVerified}" value="true" />
    </bootform:horizontalField>

    <ui:messageContainer>
        <div class="text-center">
            <g:submitButton id="btnSubmit" name="btnSubmit" class="btn btn-primary" value="Save"/>
            <g:link action="list" class="btn btn-light">Cancel</g:link>
        </div>
    </ui:messageContainer>

</g:form>

</body>
</html>
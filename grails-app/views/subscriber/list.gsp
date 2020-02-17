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
    <title>List Subscribers</title>
</head>

<body>
    <g:if test="${flash.message}">
        <ui:successContainer>${flash.message}</ui:successContainer>
    </g:if>

    <g:if test="${flash.error}">
        <ui:errorContainer>${flash.error}</ui:errorContainer>
    </g:if>

    <h1>List Subscribers</h1>

    <div class="row pad-bottom-10">
        <div class="col-sm-12">
            <g:link controller="subscriber" action="edit" class="btn btn-primary"><i class="fa fa-edit"></i> Create New Subscription</g:link>
        </div>
    </div>

    <table class="table table-bordered table-portal">
        <tr>
            <th>Email</th>
            <th>Subscribed On</th>
            <th>Active?</th>
            <th>Verified?</th>
            <th style="width: 35%;">Action</th>
        </tr>
        <g:each in="${subscribers}" var="subscriber">
            <tr>
                <td>${subscriber?.email}</td>
                <td><g:formatDate date="${subscriber?.dateCreated}" formatName="default.datetime.format"/></td>
                <td><g:formatBoolean boolean="${subscriber?.isActive}"/></td>
                <td><g:formatBoolean boolean="${subscriber?.isVerified}"/></td>
                <td>
                    <g:link action="edit" id="${subscriber?.id}" class="btn btn-sm btn-primary"><i class="fa fa-edit"></i> Edit Subscriber</g:link>
                    <g:link action="${subscriber?.isActive ? 'deactivate' : 'activate'}" id="${subscriber?.id}" class="btn btn-sm ${subscriber?.isActive ? 'btn-danger' : 'btn-primary'}"><i class="fa fa-edit"></i> ${subscriber?.isActive ? 'Unsubscribe' : 'Subscribe'}</g:link>
                </td>
            </tr>
        </g:each>
    </table>
    <ui:paginate total="${subscriberCount}" params="${params}" action="list"/>
</body>
</html>
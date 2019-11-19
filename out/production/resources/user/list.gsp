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
    <title>List Users</title>

    <g:javascript>
        $(document).ready(function(){

        })
    </g:javascript>

</head>

<body>
    <g:if test="${flash.message}">
        <ui:successContainer>${flash.message}</ui:successContainer>
    </g:if>

    <g:if test="${flash.error}">
        <ui:errorContainer>${flash.error}</ui:errorContainer>
    </g:if>

    <h1>List Users</h1>

    <div class="row pad-bottom-10">
        <div class="col-sm-12">
            <g:link controller="user" action="edit" class="btn btn-primary"><i class="fa fa-edit"></i> Create New User</g:link>
        </div>
    </div>

    <table class="table table-bordered table-portal">
        <tr>
            <th>Name</th>
            <th style="width: 10%;">Action</th>
        </tr>
        <g:each in="${users}" var="user">
            <tr>
                <td>${user.fullName}</td>
                <td>
                    <g:link action="edit" id="${user?.id}" class="btn btn-sm btn-primary"><i class="fa fa-edit"></i> Edit User</g:link>
                </td>
            </tr>
        </g:each>
    </table>

    <ui:paginate total="${totalUsers}" params="${params}" action="list"/>

</body>
</html>
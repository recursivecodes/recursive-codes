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
    <title>List Blog Posts</title>

    <g:javascript>
        $(document).ready(function(){

            $('.delete-post').on('click', function(e){
                e.preventDefault()
                var id = $( this ).data( 'id' )
                var postId = $( this ).data( 'postId' )
                var publish = !$( this ).data( 'isPublished' )
                var to = "${createLink(action: 'publish')}\?redirectTo=list" + '&postId=' + postId + '&publish=' + publish
                var confirmModal = $('#confirmDeleteModal')
                if( publish ) {
                    $('#confirmDelete').removeClass('btn-danger').addClass('btn-primary').find('#confirmDeleteLabel').html('Publish').closest('button').find('.fa').removeClass('fa-trash-o').addClass('fa-bullhorn')
                    $('#publishAction').html('publish')
                }
                else {
                    $('#confirmDelete').removeClass('btn-primary').addClass('btn-danger').find('#confirmDeleteLabel').html('Unpublish').closest('button').find('.fa').addClass('fa-trash-o').removeClass('fa-bullhorn')
                    $('#publishAction').html('unpublish')
                }
                confirmModal.modal({show: 'false'})

                $('body').on('click', '#cancelDelete', function(){
                    confirmModal.modal('hide')
                })

                $('body').on('click', '#confirmDelete', function(){
                    window.location = to
                })

                $('#confirmDeleteModal').modal('show')

            })
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

    <h1>List Blog Posts</h1>

    <div class="row pad-bottom-10">
        <div class="col-sm-12">
            <g:link controller="blog" action="edit" class="btn btn-primary"><i class="fa fa-edit"></i> Create New Blog Post</g:link>
        </div>
    </div>

    <table class="table table-bordered table-portal">
        <tr>
            <th>Title</th>
            <th>Published Date</th>
            <th>Published?</th>
            <th style="width: 35%;">Action</th>
        </tr>
        <g:each in="${posts}" var="post">
            <tr>
                <td>${post?.title}</td>
                <td><g:formatDate date="${post?.publishedDate}" formatName="default.datetime.format"/></td>
                <td><g:formatBoolean boolean="${post?.isPublished}"/></td>
                <td>
                    <g:link action="edit" id="${post?.id}" class="btn btn-sm btn-primary"><i class="fa fa-edit"></i> Edit Post</g:link>
                    <ui:toggleLink enabled="${post?.isPublished}" action="post" controller="blog" id="${post?.id}" class="btn btn-sm btn-info" target="_blank"><i class="fa fa-external-link"></i> View Post</ui:toggleLink>
                    <a href="#" data-post-id="${post?.id}" data-is-published="${post?.isPublished}" class="btn btn-sm ${post?.isPublished ? 'btn-danger' : 'btn-primary'} delete-post"><i class="fa ${post?.isPublished ? 'fa-trash-o' : 'fa-bullhorn'}"></i> ${post?.isPublished ? 'Unpublish' : 'Publish'} Post</a>
                </td>
            </tr>
        </g:each>
    </table>

    <ui:paginate total="${postCount}" params="${params}" action="list"/>

    <g:set var="footer"><button id="cancelDelete" class="btn btn-default">Cancel</button><button id="confirmDelete" class="btn btn-primary"><i class="fa"></i> <span id="confirmDeleteLabel">Publish</span></button></g:set>
    <ui:modal title="Are you sure?" id="confirmDeleteModal" closable="true" footer="${footer}">
        <div class="modal-body">
            Are you sure you want to <span id="publishAction" class="bold">publish</span> this blog post?
        </div>
    </ui:modal>

</body>
</html>
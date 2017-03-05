<%@ page import="codes.recursive.blog.Post" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
</head>
<%
    List<codes.recursive.blog.Post> posts = posts
%>
<body>
    <div class="row">
        <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
            <g:if test="${tag}">
                <h1 class="text-info">Posts tagged '${tag}'</h1>
            </g:if>
            <g:each in="${posts}" var="post">
                <div class="post-preview">
                    <g:link controller="blog" action="post" id="${post.id}">
                        <h2 class="post-title">
                            ${post.title}
                        </h2>

                        <h3 class="post-subtitle">
                            <ui:truncatePost article="${post.article}"/>
                        </h3>
                    </g:link>
                    <p class="post-meta">Posted by ${post?.authoredBy?.fullName} on <g:formatDate date="${post.publishedDate}"/></p>
                </div>
                <hr>
            </g:each>
            <ui:paginate total="${postCount}" params="${params}" />
        </div>
    </div>
</body>
</html>

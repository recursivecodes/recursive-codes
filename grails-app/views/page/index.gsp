<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
</head>
<body>
    <g:if test='${flash.message}'>
        <ui:messageContainer>
            <div class="message">${flash.message}</div>
        </ui:messageContainer>
    </g:if>
    <div class="row">
        <div class="col-lg-12">
            <g:if test="${tag}">
                <h1 class="text-info">Posts tagged '${tag}'</h1>
            </g:if>
        </div>
    </div>


    <g:each in="${posts}" var="post">
        <div class="row">
            <div class="col-lg-3 col-sm-12">
                <g:link controller="blog" action="post" id="${post.id}">
                    <img src="${post.bannerImg}" alt="${post.title}" class="img-thumbnail img-responsive img-banner-thumb">
                </g:link>
            </div>
            <div class="col-lg-9 col-sm-12">
                <div class="post-preview">
                    <g:link controller="blog" action="post" id="${post.id}">
                        <h2 class="post-title" style="margin-top: 0;">
                            ${post.title}
                        </h2>
                    </g:link>
                    <p class="post-meta">Posted by ${post?.authoredBy?.fullName} on <g:formatDate date="${post.publishedDate}"/></p>
                    <p class="post-subtitle">
                        <ui:truncatePost article="${post.article}"/>
                    </p>
                </div>
            </div>
        </div>
        <hr>
    </g:each>
    <ui:paginate total="${postCount}" params="${params}" />
</body>
</html>

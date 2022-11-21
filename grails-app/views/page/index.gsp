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

    <div class="row flex-row">
        <g:each in="${posts}" var="post" status="i">
            <div class="col-lg-4 col-md-6 col-sm-12">
                <g:link controller="blog" action="post" id="${post.id}">
                    <img src="https://res.cloudinary.com/dqcwgg5le/image/fetch/c_fit,h_250/${post.bannerImg}" alt="${post.title}" class="img-related-thumb img-thumbnail w-100" />
                    <h3>${post.title}</h3>
                </g:link>
                <p>
                    <ui:truncatePost article="${post.article}"/>
                </p>
            </div>
        </g:each>
    </div>
    <ui:paginate total="${postCount}" params="${params}" />
</body>
</html>

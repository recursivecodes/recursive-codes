<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
    <script>
        document.addEventListener('DOMContentLoaded', function(){
            const ss = "${searchString}";
            const results = document.querySelectorAll('.result');
            let i = 0;
            const regEx = new RegExp(ss, "ig");

            for (i = 0; i < results.length; ++i) {
                const it = results[i];

                const newHtml = it.innerHTML.replace(regEx, '<b>' + ss + '</b>');
                it.innerHTML = newHtml
            };
        })
    </script>
</head>

    <div class="well">
        <form id="searchForm" name="searchForm">
            <div class="row">
                <div class="col-lg-12">
                    <div class="input-group">
                        <input type="text" class="form-control" id="searchString" name="searchString" maxlength="50" value="${searchString}" placeholder="Search Blog For..." />
                        <span class="input-group-btn">
                            <button class="btn btn-primary" id="go" alt="Search" title="Search"><i class="fa fa-search"></i></button>
                        </span>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <g:if test="${!results?.count && searchString}">
        <div class="alert alert-info">This is catastrophic!!  I can't show you any results for <i><b>${searchString}</b></i>!</div>
    </g:if>

    <g:if test="${results.count}">
        <h1 class="mb-5">Found ${results.count} post${results.count > 1 ? 's' : ''} matching <i><b>${searchString}</b></i>.</h1>

        <g:each in="${results.posts}" var="post">
            <div class="post-preview">
                <g:link controller="blog" action="post" id="${post.id}">
                    <div>
                        <b>${post.title}</b>
                    </div>
                </g:link>
                <div class="text-success">
                    <small><i>${baseUrl}${createLink( action:'post',params: [id: post.id] )}</i></small>
                </div>
                <div>
                    <small class="text-muted result"><ui:truncatePost context="${searchString}" contentLength="250" article="${post.article}"/></small>
                </div>
                <p class="post-meta">Posted by ${post?.authoredBy?.fullName} on <g:formatDate date="${post.publishedDate}"/></p>
            </div>
        </g:each>

        <ui:paginate total="${results.count}" params="${params}" action="search"/>

    </g:if>
</body>
</html>

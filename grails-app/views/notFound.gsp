<!doctype html>
<html>
    <head>
        <title>Page Not Found</title>
        <meta name="layout" content="main">
        <g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
    </head>
    <body>
        <div>
            <h1 class="ml-0 mr-0 mb-3">Oops</h1>
            <asset:image src="404.png" class="img-responsive" />
            <h2 class="ml-0 mr-0 mt-3">Nothing found at ${request.forwardURI}</h2>
        </div>
    </body>
</html>

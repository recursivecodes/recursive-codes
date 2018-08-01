<!doctype html>
<html>
    <head>
        <title>Page Not Found</title>
        <meta name="layout" content="main">
        <g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
    </head>
    <body>
        <h1>Oops</h1>
        <img src="https://fontmeme.com/permalink/180801/0c97a5477cc55271af8c8214e73f78e5.png" alt="404">
        <h2>Nothing found at ${request.forwardURI}</h2>
    </body>
</html>

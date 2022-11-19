<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
</head>
<body>
    <h1>Videos</h1>

    <div>
        <p>Sometimes I create video tutorials and host them on my <a href="${channelUrl}">YouTube channel</a>.  Here are some of those videos!</p>
    </div>

    <div>
        <p>If you'd like to keep up to date on my videos, please subscribe!</p>
    </div>

    <div class="pad-bottom-20">
        <script src="https://apis.google.com/js/platform.js"></script>
        <div class="d-flex">
            <div class="border border-dark rounded p-3">
                <div class="g-ytsubscribe" data-channelid="UCTItj4gSM-1_fiTHzJRJqxQ" data-layout="full" data-count="default"></div>
            </div>
        </div>
    </div>

    <g:if test="${!entries?.entry?.size()}">
        <div class="alert alert-info"><b>Hey There!</b> It looks like I haven't posted any videos lately (or something went wrong trying to pull the video feed).  Check back later!</div>
    </g:if>

    <div class="row" id="videos">
        <g:each in="${entries.entry}" var="entry" status="i">
            <g:if test="${i < 12}">
                <div class="col-xs-12 col-lg-6 mb-5 video-container">
                    <div class="ratio ratio-16x9">
                        <iframe width="560" height="315" src="https://www.youtube.com/embed/${entry.'yt:videoId'}" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
                    </div>
                </div>
            </g:if>
        </g:each>
    </div>

    <g:if test="${entries.entry.size() > 12}">
        <div>
            <b>But wait, there's more!!  Please check out <a href="${channelUrl}">my channel on YouTube</a> to see the rest.</b>
        </div>
    </g:if>

</body>
</html>

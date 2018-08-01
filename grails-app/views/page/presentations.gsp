<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
</head>
<body>
    <h1>Presentations</h1>

    <div>
        <p>Here are some of my recent presentations.</p>
    </div>

    <div class="row">
        <g:each in="${presentations}" var="presentation">
            <div class="col-xs-12">
                <div class="row mb-5 ">
                    <div class="col-xs-12 col-lg-7 my-auto">
                        <iframe src="//slides.com/recursivecodes/${presentation.alias}/embed" width="100%" height="420" scrolling="no" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>
                    </div>
                    <div class="col-xs-12 col-lg-5 ">
                        <h2 class="mt-3 mt-lg-0">${presentation.title}</h2>
                        <div>
                            <div class="abstract">
                                ${presentation.abstract}
                            </div>
                            <h4>Presented at:</h4>
                            <ul class="list-unstyled">
                                <g:each in="${presentation.conferences}" var="conference">
                                    <li>
                                        ${conference.date}: <a href="${conference.link}">${conference.name}</a>
                                        <g:if test="${conference?.recording.size()}">
                                            <ul class="">
                                                <li><a href="${conference.recording}"><i class="fa fa-video-camera mr-3"></i>Watch This Presentation</a></li>
                                            </ul>
                                        </g:if>
                                    </li>
                                </g:each>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </g:each>
    </div>

</body>
</html>

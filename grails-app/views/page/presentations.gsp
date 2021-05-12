<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
</head>
<body>
    <div class="row">
        <div class="col-xs-12">
            <h1>Presentations</h1>
            <div>
                <p>Here are some of my recent presentations.</p>
            </div>
        </div>
    </div>
    <div class="row">
        <g:each in="${presentations}" var="presentation">
            <div class="col-xs-12">

                <div class="panel panel-default">
                    <div class="panel-heading">${presentation.title}</div>
                    <div class="panel-body">
                        <div class="row ">
                            <ui:modal large="true" closable="true" id="slides_${presentation['google-embed-id']}" title="${presentation.title}">
                                <iframe src="https://drive.google.com/file/d/${presentation['google-embed-id']}/preview" width="100%" height="480" style="height: 480px;"></iframe>
                            </ui:modal>
                            <div class="col-xs-12 col-lg-12">
                                <div>
                                    ${presentation.abstract}
                                </div>
                                <div class="table-responsive pt-5">
                                    <table class="table">
                                        <tr>
                                            <th>Conference/Webinar</th>
                                            <th>Date</th>
                                            <th>Recording</th>
                                        </tr>
                                        <g:each in="${presentation.conferences}" var="conference">
                                            <tr>
                                                <td><a href="${conference.link}">${conference.name}</a></td>
                                                <td>${conference.date}</td>
                                                <td>
                                                    <g:if test="${conference?.recording.size()}">
                                                        <a href="${conference.recording}"><i class="fa fa-video-camera mr-3"></i>Watch Recording</a>
                                                    </g:if>
                                                    <g:else>
                                                        None Available
                                                    </g:else>
                                                </td>
                                            </tr>
                                        </g:each>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <a href="#" class="btn btn-primary" onclick="$('#slides_${presentation['google-embed-id']}').modal('show'); return false;">View Slides</a>
                    </div>
                </div>


            </div>
        </g:each>
    </div>
    <ui:paginate total="${totalPresentations}" offset="${offset}" max="${max}" params="${params}" action="presentations"/>
</body>
</html>

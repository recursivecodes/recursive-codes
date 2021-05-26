<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
    <script src="https://documentcloud.adobe.com/view-sdk/main.js"></script>
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
        <g:each in="${presentations}" var="presentation" status="p">
            <div class="col-xs-12">

                <div class="panel panel-default">
                    <div class="panel-heading">${presentation.title}</div>
                    <div class="panel-body">
                        <div class="row ">
                            <div data-presentation-alias="${presentation.alias}" id="presentation_${p}" class="hide pdf-embed"></div>
                            <%--
                            <ui:modal large="true" closable="true" id="presentation_modal_${p}" title="${presentation.title}">
                                <div data-presentation-alias="${presentation.alias}" id="presentation_${p}" class="pdf-embed"></div>
                            </ui:modal>
                            --%>
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
                        <a href="#" class="btn btn-primary show-presentation-btn" data-presentation-id="${p}" data-presentation-modal-id="presentation_${p}" >View Slides</a>
                    </div>
                </div>


            </div>
        </g:each>
    </div>
    <ui:paginate total="${totalPresentations}" offset="${offset}" max="${max}" params="${params}" action="presentations"/>

    <script>
    const ADOBE_KEY = 'ac5c88152be14041833345afd9a99393';
    //const ADOBE_KEY = '4e6dfc2d75484f3ab8cc8403d69af0a6'; // localhost

    $('.show-presentation-btn').on('click', (e) => {
        let pId = $(e.currentTarget).data('presentationId');
        let presentationContainer = 'presentation_' + pId;
        let alias = $('#' + presentationContainer).data('presentationAlias');
        //$('#presentation_modal_' + pId).modal('show');
        let adobeDCView = new AdobeDC.View({clientId: ADOBE_KEY, divId: presentationContainer});
            adobeDCView.previewFile({
            content: {
                location: {
                    url: 'https://objectstorage.us-ashburn-1.oraclecloud.com/n/idatzojkinhi/b/presentations.recursive.codes/o/' + alias + '.pdf'
                }
            },
            metaData: {
                fileName: alias + '.pdf'
            }
        },
        {
            embedMode: "LIGHT_BOX" // IN_LINE
        });
        const options = {
            enablePDFAnalytics: true
        }
        adobeDCView.registerCallback(
            AdobeDC.View.Enum.CallbackType.EVENT_LISTENER,
            function(event) {
                if( event.type == 'DOCUMENT_OPEN' ) {
                    const title = event.data.fileName.replace('.pdf', '');
                    $('[data-presentation-alias="'+title+'"]').removeClass('hide')
                }
                if( event.type == 'PDF_VIEWER_CLOSE' ) {
                  $('.pdf-embed').addClass('hide');
                }
            },
            options
        );

        return false;
    });

    function enablePDF() {
        let btn = document.querySelector('#showPDF');
        btn.addEventListener('click', () => displayPDF());
        btn.disabled = false;
    }

</script>
</body>
</html>

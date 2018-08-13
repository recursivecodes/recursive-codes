<%--
  Created by IntelliJ IDEA.
  User: 558848
  Date: 3/7/2016
  Time: 11:08 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="admin"/>
    <title>Edit Blog Post</title>
    <g:javascript>
        currentTags = ${raw((command.tags.collect { it.class.name == 'java.lang.String' ? it : it.id.toString() } as grails.converters.JSON) as String)};
        dateFormat = '${g.message(code: 'js.dateTimeFormat')}';
        youTubeApiKey = '${youTubeApiKey}';
        youTubeChannelId = '${youTubeChannelId}';
        function googleApiClientReady() {
            gapi.client.setApiKey(youTubeApiKey);
            gapi.client.load('youtube', 'v3', function() {});
        }
    </g:javascript>
    <asset:javascript src="rivets/rivets.js"/>
    <script src="${assetPath(src: 'edit-post.js')}" type="module"></script>
    <script src="https://apis.google.com/js/client.js?onload=googleApiClientReady"></script>
    <asset:javascript src="ace/src-min/ace.js"/>
    <asset:javascript src="wysihtml5/wysihtml.min.js"/>
    <asset:javascript src="wysihtml5/wysihtml.all-commands.min.js"/>
    <asset:javascript src="wysihtml5/wysihtml.toolbar.min.js"/>
    <asset:javascript src="wysihtml5/parser_rules/advanced_and_extended.js"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/postscribe/2.0.8/postscribe.min.js"></script>

    <style>
    .wysihtml-action-active, .wysihtml-command-dialog-opened,
    .wysihtml-command-active {
        box-shadow: inset 0 1px 6px rgba(0, 0, 0, 0.2);
        background: #eee !important;
    }
    .wysihtml-sandbox{
        height: 550px !important;
        width: 100% !important;
    }
    .fullscreen .wysihtml-sandbox {
        height: calc(100vh - 130px) !important;
    }
    .ace_editor {
        height: 350px;
    }
    .dropzone {
        border: 1px dashed red !important;
        pointer-events: none;
    }
    .nicehide {
        resize: none !important;
        overflow: hidden !important;
        display: block !important;
        width: 0 !important;
        max-width: 0 !important;
        height: 0 !important;
        max-height: 0 !important;
        margin: 0 0 0 15px;
        padding: 1px !important;
        float: left;
        border: none;
    }
    </style>
</head>

<body>
    <div class="d-flex">
        <div class="align-self-center">
            <h1 class="mt-0">${params.id ? 'Edit' : 'Create'} Blog Post</h1>
        </div>
        <div class="align-self-center ml-auto">
            <g:link controller="blog" action="list" class="btn btn-primary"><i class="fa fa-angle-double-left"></i> List Blog Posts</g:link>
        </div>
    </div>

    <div class="pad-bottom-10">
        <g:if test="${flash.message}">
            <ui:successContainer>${flash.message}</ui:successContainer>
        </g:if>

        <g:if test="${flash.error}">
            <ui:errorContainer>${flash.error}</ui:errorContainer>
        </g:if>
    </div>

    <g:form useToken="true" name="postForm" action="${actionName}" method="POST" class="" params="${defaultParams}">
        <div class="d-flex">
            <div class="mr-5" style="width: 400px; min-height: calc(100vh - 160px); margin-bottom: 60px;">
                <ui:panel title="Meta">
                    <g:hiddenField name="id" value="${defaultParams.id}"/>
                    <g:hiddenField name="version" id="version" value="${command.version}"/>
                    <g:hiddenField name="authoredBy.id" value="${command.authoredBy?.id ?: currentUser.id}"/>
                    <g:hiddenField name="lastUpdatedBy.id" value="${currentUser.id}"/>

                    <bootform:field field="title" required="true" labelColumnClass="col-sm-2" controlColumnClass="col-sm-8" label="Title" bean="${command}" description="The title of this blog post.  Used as the 'headline' for the article.">
                        <g:textField type="text" id="title" name="title" class="form-control" required="true" maxlength="250" value="${command.title}"/>
                    </bootform:field>

                    <bootform:field field="keywords" required="false" labelColumnClass="col-sm-2" controlColumnClass="col-sm-8" label="Keywords" bean="${command}" description="Keywords to associate with the metadata of this post.  The post's tags will also be included as keywords.">
                        <g:textField type="text" id="keywords" name="keywords" class="form-control" maxlength="500" value="${command?.keywords}"/>
                    </bootform:field>

                    <bootform:field field="summary" required="false" labelColumnClass="col-sm-2" controlColumnClass="col-sm-8" label="Summary" bean="${command}" description="The meta description to be included with this post.">
                        <g:textArea id="summary" name="summary" class="form-control" rows="8" maxlength="500" value="${command?.summary}"/>
                    </bootform:field>

                    <bootform:field field="publishedDate" required="true" labelColumnClass="col-sm-2" controlColumnClass="col-sm-8" label="Published Date" bean="${command}" description="The date/time that this blog post will be published.">
                        <g:textField type="text" id="publishedDate" name="publishedDate" class="form-control datepicker" maxlength="250" value="${g.formatDate(date: command.publishedDate, formatName: 'default.datetime.format')}"/>
                    </bootform:field>

                    <g:set var="tagDesc">
                        The categories that this blog post will be filed under.  <a href="#" id="addTagBtn" rv-on-click="model.addTag">Add a tag.</a>
                    </g:set>
                    <bootform:field field="tags" required="true" labelColumnClass="col-sm-2" controlColumnClass="col-sm-8" label="Tags" bean="${command}" description="${tagDesc}">
                        <g:select type="text" id="postTags" name="tags" required="true" from="[]" class="form-control" multiple="multiple"></g:select>
                    </bootform:field>

                    <bootform:field field="isPublished" bean="${command}">
                        <bootform:checkboxContainer labelOffsetClass="col-sm-offset-2" disabled="false" bean="${command}" field="isPublished" horizontal="false">
                            <g:checkBox value="1" checked="${command.isPublished}" name="isPublished" id="isPublished"/>
                            Published?
                        </bootform:checkboxContainer>
                    </bootform:field>
                </ui:panel>
            </div>

            <div class="w-100" style="min-height: calc(100vh - 160px)">
                <div tabindex="0" id="editor" rv-on-keydown="model.exitFullScreen" >
                    <bootform:field field="article" required="true" labelColumnClass="col-sm-2" controlColumnClass="col-sm-8" bean="${command}" description="">
                        <div id="toolbar" style="display: none;" class="pad-bottom-10">
                            <div class="btn-group">
                                <%--
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="justifyLeft" title="Left align text"><i class="fa fa-align-left"></i></a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="justifyCenter" title="Center text"><i class="fa fa-align-center"></i></a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="justifyRight" title="Right align text"><i class="fa fa-align-right"></i></a>
                                --%>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="bold" title="Make text bold (CTRL + B)"><i class="fa fa-bold"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="italic" title="Make text italic (CTRL + I)"><i class="fa fa-italic"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="underline" title="Make text underlined (CTRL + u)"><i class="fa fa-underline"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="strikethrough" title="Make text strikethrough"><i class="fa fa-strikethrough"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="createLink" title="Insert a link" class="command" href="javascript:;" unselectable="on"><i class="fa fa-link"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="insertImage" title="Insert an image" class="command" href="javascript:;" unselectable="on"><i class="fa fa-picture-o"></i>
                                </a>
                                <a class="btn btn-sm btn-default browse-s3-trigger" rv-on-click="model.showS3Browser" data-toggle="tooltip" data-container="body" title="Browse S3" href="javascript:;" unselectable="on"><i class="fa fa-amazon"></i>
                                </a>
                                <a class="btn btn-sm btn-default upload-s3-trigger" data-toggle="tooltip" rv-on-click="model.showS3UploadModal" data-container="body" title="Upload To S3" href="javascript:;" unselectable="on"><i class="fa fa-upload"></i>
                                </a>
                            </div>

                            <div class="btn-group">
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="insertHTML" data-wysihtml-command-value="[spoiler label=Spoiler]Spoiler Text Here[/spoiler]" title="Insert a spoiler tag" class="command" href="javascript:;" unselectable="on"><i class="fa fa-exclamation-circle"></i>
                                </a>
                                <a class="btn btn-sm btn-default create-gist-trigger" rv-on-click="model.showGistModal" data-toggle="tooltip" data-container="body" title="Create a gist" class="command" href="javascript:;" unselectable="on"><i class="fa fa-github-alt"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="insertHTML" data-wysihtml-command-value="[gist2 id=]" title="Insert a gist" class="command" href="javascript:;" unselectable="on"><i class="fa fa-github"></i>
                                </a>
                                <a class="btn btn-sm btn-default youtube-search-trigger" rv-on-click="model.showYouTubeModal" data-toggle="tooltip" data-container="body" title="Embed a YouTube video" href="javascript:;" unselectable="on"><i class="fa fa-youtube"></i>
                                </a>
                            </div>

                            <div class="btn-group">
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="insertHTML" data-wysihtml-command-value="<div class='alert alert-info'>Info</div>" title="Insert an info alert" class="command" href="javascript:;" unselectable="on"><i class="fa fa-exclamation text-info"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="insertHTML" data-wysihtml-command-value="<div class='alert alert-success'>Success</div>" title="Insert a success alert" class="command" href="javascript:;" unselectable="on"><i class="fa fa-smile-o text-success"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="insertHTML" data-wysihtml-command-value="<div class='alert alert-warning'>Warning</div>" title="Insert a warning alert" class="command" href="javascript:;" unselectable="on"><i class="fa fa-exclamation-triangle text-warning"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="insertHTML" data-wysihtml-command-value="<div class='alert alert-danger'>Danger</div>" title="Insert a danger alert" class="command" href="javascript:;" unselectable="on"><i class="fa fa-minus-circle text-danger"></i>
                                </a>
                            </div>

                            <div class="btn-group">
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="insertUnorderedList" title="Insert an unordered list"><i class="fa fa-list-ul"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="insertOrderedList" title="Insert an ordered list"><i class="fa fa-list-ol"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="formatBlock" data-wysihtml-command-value="p" title="Insert paragraph"><i class="fa fa-paragraph"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="formatInline" data-wysihtml-command-value="code" title="Insert code"><i class="fa fa-code"></i>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="formatInline" data-wysihtml-command-value="pre" title="Insert pre"><small>pre</small>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="insertBlockQuote" class="wysihtml-command-active" title="Insert blockquote"><i class="fa fa-quote-right"></i>
                                </a>
                            </div>

                            <div class="btn-group">
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="formatBlock" data-wysihtml-command-value="h1" title="Insert headline 1" style=""><span style="font-size: 12px;">&nbsp;</span><i class="fa fa-header">1</i><span style="font-size: 12px;">&nbsp;</span>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="formatBlock" data-wysihtml-command-value="h2" title="Insert headline 2" style=""><span style="font-size: 12px;">&nbsp;</span><i class="fa fa-header">2</i><span style="font-size: 12px;">&nbsp;</span>
                                </a>
                                <a class="btn btn-sm btn-default" data-toggle="tooltip" data-container="body" data-wysihtml-command="formatBlock" data-wysihtml-command-value="h3" title="Insert headline 3" style=""><span style="font-size: 12px;">&nbsp;</span><i class="fa fa-header">3</i><span style="font-size: 12px;">&nbsp;</span>
                                </a>
                            </div>

                            <div class="btn-group">
                                <a class="btn btn-sm btn-default action full-screen-trigger" rv-on-click="model.goFullscreen" href="javascript:;" unselectable="on" data-toggle="tooltip" data-container="body" title="Full Screen" unselectable="on"><i class="fa fa-arrows-alt"></i>
                                </a>
                                <a class="btn btn-sm btn-default action" id="viewSourceBtn" rv-on-click="model.viewSource" href="javascript:;" unselectable="on" data-toggle="tooltip" data-container="body" data-wysihtml-action="change_view" title="Show HTML"><i class="fa fa-code"></i>
                                </a>
                                <a class="btn btn-sm btn-default help-modal-trigger pointer" rv-on-click="model.showHelpModal" data-toggle="tooltip" data-container="body" title="Blog Editor Help" unselectable="on"><i class="fa fa-question"></i>
                                </a>
                            </div>

                            <div data-wysihtml-dialog="createLink" style="display: none;" class="pad-top-10">
                                <div class="well">
                                    <label>
                                        Link:
                                        <input data-wysihtml-dialog-field="href" value="http://" class="form-control">
                                    </label>
                                    <label>
                                        Target:
                                        <input data-wysihtml-dialog-field="target" value="_blank" class="form-control">
                                    </label>
                                    <a data-wysihtml-dialog-action="save" class="btn btn-primary btn-sm">OK</a>&nbsp;<a data-wysihtml-dialog-action="cancel" class="btn btn-sm btn-default">Cancel</a>
                                </div>
                            </div>
                            <div data-wysihtml-dialog="insertImage" style="display: none;" class="pad-top-10">
                                <div class="well">
                                    <label>
                                        Src:
                                        <input data-wysihtml-dialog-field="src" value="http://" class="form-control">
                                    </label>
                                    <label>
                                        Class:
                                        <input data-wysihtml-dialog-field="class" value="img-responsive" class="form-control">
                                    </label>
                                    <a data-wysihtml-dialog-action="save" class="btn btn-primary btn-sm">OK</a>&nbsp;<a data-wysihtml-dialog-action="cancel" class="btn btn-sm btn-default">Cancel</a>
                                </div>
                            </div>
                        </div>

                        <textarea id="wysihtml-textarea" name="article" rows="25" class="form-control" required="required">
                            ${raw(fieldValue(bean: command, field: 'article').decodeHTML())}
                        </textarea>

                    </bootform:field>
                    <div id="editPostFooter">
                        <div class="">
                            <div class="text-center">
                                <button id="btnSubmit" rv-on-click="model.submitClicked" name="btnSubmit" class="btn btn-primary">Save</button>
                                <a class="btn btn-sm btn-info preview-post-trigger pointer" rv-on-click="model.previewPost" data-toggle="tooltip" data-container="body" title="Preview Post" unselectable="on"><i class="fa fa-binoculars"></i> Preview Post</a>
                                <g:link action="list" class="btn btn-default">Cancel</g:link>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </g:form>

    <g:set var="imageEditorFooter">
        <button id="saveEditedImgBtn" rv-on-click="model.uploadPastedImage" type="button" class="btn btn-primary">Upload</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
    </g:set>
    <ui:modal large="true" closable="true" id="editImageModal" title="Image Uploader" footer="${imageEditorFooter}">
        <div class="form-group">
            <input type="text" id="editedImageName" name="editedImageName" placeholder="Name" maxlength="100" class="form-control" />
        </div>
        <div id="imageEditor"></div>
    </ui:modal>

    <ui:modal large="true" closable="true" id="previewModal" title="Preview Post">
        <div id="previewBody"></div>
    </ui:modal>

    <ui:modal large="true" closable="true" id="youTubeSearchModal" title="Search YouTube">
        <div class="row">
            <div class="col-xs-12 col-lg-4">
                <div class="form-group">
                    <select name="youTubeChannel" id="youTubeChannel" class="form-control">
                        <option rv-value="model.myChannelId">My Channel</option>
                        <option value="">All Channels</option>
                    </select>
                </div>
            </div>
            <div class="col-xs-12 col-lg-8">
                <div class="form-group">
                    <div class="input-group">
                        <input type="text" id="youTubeSearchString" name="youTubeSearchString" placeholder="Search" maxlength="100" class="form-control" />
                        <span class="input-group-btn">
                            <button class="btn btn-primary" id="youTubeSearchBtn" name="youTubeSearchBtn" rv-on-click="model.doYouTubeSearch"><i class="fa fa-search"></i></button>
                        </span>
                    </div>
                </div>
            </div>
        </div>
        <div class="alert alert-info" rv-hide="model.youTubeHasResults">
            <i class="fa fa-info-circle"></i> There are no results to show.
        </div>
        <div class="row">
            <div rv-each-item="model.youTubeResults">
                <div class="col-xs-3 pb-3">
                    <div class="thumbnail">
                        <a href="#" rv-on-click="model.selectYouTube">
                            <img rv-src="item.snippet.thumbnails.medium.url" rv-title="item.snippet.title" class="img-responsive" />
                        </a>
                        <!--
                        <div class="caption">
                            <div>{item.snippet.title}</div>
                        </div>
                        -->
                    </div>
                </div>
            </div>
        </div>
    </ui:modal>

    <g:set var="createGistFooter">
        <button id="createGistBtn" rv-on-click="model.createGist" type="button" class="btn btn-primary">Create</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
    </g:set>
    <ui:modal large="true" closable="true" id="createGistModal" title="Create A Gist" footer="${createGistFooter}">
        <div>
            <div class="form-group">
                <input type="text" id="createGistName" rv-on-keydown="model.setAceMode" name="createGistName" placeholder="Name" maxlength="100" class="form-control" />
            </div>
            <div class="form-group">
                <input type="text" id="createGistDescription" name="createGistDescription" placeholder="Description" maxlength="100" class="form-control" />
            </div>
            <div class="form-group">
                <textarea placeholder="Code" name="createGistCode" id="createGistCode" rows="10" class="form-control"></textarea>
            </div>
        </div>
    </ui:modal>

    <g:set var="uploadFooter">
        <button id="uploadFileBtn" rv-on-click="model.s3Upload" type="button" class="btn btn-primary">Upload</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
    </g:set>
    <ui:modal large="true" closable="true" id="s3UploadModal" title="Upload To S3" footer="${uploadFooter}">
        <form>
            <!--
            <div style="height: 100px;" class=" d-flex mb-3 w-100 well"
                 rv-on-drop="model.doDrop"
                 rv-on-dragenter="model.doDragEnter" rv-on-dragleave="model.doDragLeave"
                 rv-on-dragover="model.doDragOver" rv-on-dragend="model.doDragEnd" draggable="true">
                <div class="align-self-center text-center w-100">
                    <small class="text-muted">Drag Files Here</small>
                </div>
            </div>
            -->
            <div class="row" rv-each-upload="model.s3Uploads" rv-value="index">
                <div class="col-xs-4">
                    <div class="form-group">
                        <label rv-for="index | prepend 'uploadFolder_'" class="control-label folder-label">Folder</label>
                        <div class="input-group">
                            <span class="input-group-btn">
                                <button class="btn btn-danger remove-upload" rv-enabled="model.canRemoveUpload" rv-on-click="model.removeS3Upload"><i class="fa fa-times"></i></button>
                            </span>
                            <input type="text" rv-value="upload.folder" rv-id="index | prepend 'uploadFolder_'" class="form-control upload-folder" rv-name="index | prepend 'uploadFolder_'" />
                        </div>
                        <p class="help-block"><small>Leave blank for 'root'</small></p>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group">
                        <label rv-for="index | prepend 'uploadKey_'" class="control-label key-label">Key</label>
                        <input type="text" rv-value="upload.key" rv-id="index | prepend 'uploadKey_'" class="form-control upload-key" rv-name="index | prepend 'uploadKey_'" />
                        <p class="help-block"><small>Defaults to file name</small></p>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group">
                        <label rv-for="index | prepend 'uploadFile_'" class="control-label file-label">File</label>
                        <input type="file" rv-value="upload.file" class="form-control upload-file" rv-id="index | prepend 'uploadFile_'" rv-name="index | prepend 'uploadFile_'" />
                    </div>
                </div>
            </div>

            <div>
                <button class="btn btn-success" id="addUploadBtn" rv-on-click="model.addUpload"><i class="fa fa-plus"></i> Add File</button>
            </div>
        </form>
    </ui:modal>

    <ui:modal large="true" closable="true" id="s3Modal" title="Browse S3">
        <iframe id="s3BrowserIframe" src="https://s3.amazonaws.com/${imgBucket}/index.html" width="100%" height="400px" frameborder="0"></iframe>
    </ui:modal>

    <g:set var="footer">
        <button id="saveNewTagBtn" rv-on-click="model.saveNewTag" type="button" class="btn btn-primary">Save Tag</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
    </g:set>

    <ui:modal closable="true" id="newTagModal" title="Create A New Tag" footer="${footer}">
        <div class="form-group">
            <input type="text" id="newTag" name="newTag" class="form-control" maxlength="50"/>
        </div>
    </ui:modal>

    <ui:modal closable="true" id="helpModal" title="Editor Help">
        <h4>You can use the following tags in a blog post:</h4>
        <p>
            <b>[gist id=]</b>:  Insert a gist from github.  Pass the ID from the URL.  Shortcut:  click the GitHub button in the toolbar.
        </p>
        <p>
            <b>[youtube id=]</b>:  Embed a YouTube video.  Pass the ID from the URL.  Shortcut:  click the YouTube button in the toolbar.
        </p>
        <p>
            <b>[spoiler label="Spoiler"]content[/spoiler]</b>:  Hide content until the reader clicks on the link rendered in the blog post.  Pass a 'label' or what will be used as the trigger text for the spoiler content.  Shortcut:  click the 'spoiler' button in the toolbar.
        </p>
    </ui:modal>
</body>
</html>
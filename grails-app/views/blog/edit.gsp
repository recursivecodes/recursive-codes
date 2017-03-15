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
        // ${raw(command.tags)}
        currentTags = ${raw((command.tags.collect { it.class.name == 'java.lang.String' ? it : it.id.toString() } as grails.converters.JSON) as String)}

        $(document).ready(function(){

            $('.datepicker').datetimepicker({ format: '${g.message(code: 'js.dateTimeFormat')}'})
            editor = new wysihtml.Editor(
                    "wysihtml-textarea", {
                        toolbar: "toolbar",
                        parserRules: wysihtmlParserRules
                    }
            )
            editor.on( "load", function() {
                // Trick browser into showing HTML5 required validation popups.
                $('#wysihtml-textarea').addClass('nicehide');
            } );

            $('#addTagBtn').on('click', function(){
                $('#newTagModal').modal({show: true})
                $('#newTag').val('')
            })

            $('#saveNewTagBtn').on('click', function(){
                var tagEl = $('#newTag')
                if( !tagEl.val().length ) {
                    tagEl.closest('.form-group').addClass('has-error')
                }
                else {
                    tagEl.closest('.form-group').removeClass('has-error')

                    $.ajax({
                        url:   '/blog/ajaxSaveTag?tag=' + tagEl.val(),
                        success: function(result){
                            $('#newTagModal').modal('hide')
                            currentTags = $('#postTags').val()
                            listTags()
                        },
                        error: function(){
                            alert('An error occurred trying to save this tag.  Please try again.')
                        }
                    })
                }
            })

            $('#viewSourceBtn').on('click', function(){
                if( $('.nicehide').size() > 0 ) {
                    $('.nicehide').addClass('nonicehide').removeClass('nicehide')
                }
            })

            $('#btnSubmit').on('click', function(){
                if( $('.nonicehide').size() > 0 ) {
                    $('.nonicehide').addClass('nicehide').removeClass('nonicehide')
                }
            })

            listTags()
        })

        listTags = function(){
            $.ajax(
                    {
                        url: '/blog/ajaxListTags',
                        success: function(result){
                            var sel = $('#postTags')
                            sel.html('')

                            $(result).each(function(i,e){
                                var opt = $('<option value="' + e.id + '">' + e.name + '</option>')
                                if( $.inArray(e.id.toString(), currentTags) != -1 ) {
                                    opt.attr('selected', 'selected')
                                }
                                sel.append(opt)
                            })
                        },
                        error: function(){
                            alert('An error occurred trying to list tags.  Please try again.')
                        }
                    }
            )
        }

    </g:javascript>
    <asset:javascript src="wysihtml5/wysihtml.min.js"/>
    <asset:javascript src="wysihtml5/wysihtml.all-commands.min.js"/>
    <asset:javascript src="wysihtml5/wysihtml.toolbar.min.js"/>
    <asset:javascript src="wysihtml5/parser_rules/advanced.js"/>
    <style>
    .wysihtml-action-active, .wysihtml-command-dialog-opened,
    .wysihtml-command-active {
        box-shadow: inset 0 1px 6px rgba(0, 0, 0, 0.2);
        background: #eee !important;
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

    <h1>${params.id ? 'Edit' : 'Create'} Blog Post</h1>

    <div class="pad-bottom-10">
        <g:link controller="blog" action="list" class="btn btn-primary"><i class="fa fa-angle-double-left"></i> List Blog Posts</g:link>
    </div>

    <div class="pad-bottom-10">
        <g:if test="${flash.message}">
            <ui:successContainer>${flash.message}</ui:successContainer>
        </g:if>

        <g:if test="${flash.error}">
            <ui:errorContainer>${flash.error}</ui:errorContainer>
        </g:if>
    </div>

    <g:form useToken="true" action="${actionName}" method="POST" class="form-horizontal" params="${defaultParams}">
        <g:hiddenField name="version" value="${command.version}"/>
        <g:hiddenField name="authoredBy.id" value="${command.authoredBy?.id ?: currentUser.id}"/>
        <g:hiddenField name="lastUpdatedBy.id" value="${currentUser.id}"/>

        <bootform:horizontalField field="title" required="true" labelColumnClass="col-sm-2" label="Title" bean="${command}" description="The title of this blog post.  Used as the 'headline' for the article.">
            <g:textField type="text" id="title" name="title" class="form-control" required="true" maxlength="250" value="${command.title}"/>
        </bootform:horizontalField>


        <bootform:horizontalField field="publishedDate" required="true" labelColumnClass="col-sm-2" label="Published Date" bean="${command}" description="The date/time that this blog post will be published.">
            <g:textField type="text" id="publishedDate" name="publishedDate" class="form-control datepicker" maxlength="250" value="${g.formatDate(date: command.publishedDate, formatName: 'default.datetime.format')}"/>
        </bootform:horizontalField>

        <g:set var="tagDesc">
            The categories that this blog post will be filed under.  <a href="#" id="addTagBtn">Add a tag.</a>
        </g:set>
        <bootform:horizontalField field="tags" required="true" labelColumnClass="col-sm-2" label="Tags" bean="${command}" description="${tagDesc}">
            <g:select type="text" id="postTags" name="tags" required="true" from="[]" class="form-control" multiple="multiple"></g:select>
        </bootform:horizontalField>

        <bootform:fieldContainer field="isPublished" bean="${command}">
            <bootform:checkboxContainer labelOffsetClass="col-sm-offset-2" disabled="false" bean="${command}" field="isPublished" horizontal="true">
                <g:checkBox value="1" checked="${command.isPublished}" name="isPublished" id="isPublished"/>
                Published?
            </bootform:checkboxContainer>
        </bootform:fieldContainer>

        <bootform:horizontalField field="article" required="true" labelColumnClass="col-sm-2" label="Article" bean="${command}" description="">

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
                    <a class="btn btn-sm btn-default action" id="viewSourceBtn" href="javascript:;" unselectable="on" data-toggle="tooltip" data-container="body" data-wysihtml-action="change_view" title="Show HTML"><i class="fa fa-code"></i>
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

        </bootform:horizontalField>

        <ui:messageContainer>
            <div class="text-center">
                <g:submitButton id="btnSubmit" name="btnSubmit" class="btn btn-primary" value="Save"/>
                <g:link action="list" class="btn btn-default">Cancel</g:link>
            </div>
        </ui:messageContainer>

    </g:form>

    <div id="newTagModal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">Create A New Tag</h4>
                </div>

                <div class="modal-body">
                    <div class="form-group">
                        <input type="text" id="newTag" name="newTag" class="form-control" maxlength="50"/>
                    </div>
                </div>

                <div class="modal-footer">
                    <button id="saveNewTagBtn" type="button" class="btn btn-primary">Save Tag</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</body>
</html>
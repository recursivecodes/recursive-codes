"use strict";
import { Upload } from "./model/upload.js";

rivets.formatters.showRemoveBtn = (value) => value.length > 1;
rivets.formatters.gt = function(value, args) {
    return value > args;
};
rivets.formatters.length = function(value) {
    return value ? (value.length || 0) : 0;
};
rivets.formatters.prepend = function(value, prepend) {
    return prepend + value
};

rivets.formatters.append = function(value, append) {
    return value + append
};

const model = {
    myChannelId: youTubeChannelId,
    youTubeResults: [],
    editor: null,
    aceEditor: null,
    s3Uploads: [],
    selectYouTube: function(event, context) {
        const item = context.item;
        const id = item.id.videoId;
        model.editor.focus();
        model.editor.composer.commands.exec("insertHTML","[youtube id=" + id + "]");
        $('#youTubeSearchModal').modal('hide')
    },
    doYouTubeSearch: function() {
        model.youTubeResults = [];
        const results = model.searchYouTube( $('#youTubeSearchString').val(), $('#youTubeChannel').val() );
        results.then(function(result){
            model.youTubeResults = result.result.items;
        })
    },
    searchYouTube: function(q, channelId) {
        const params = {
            q: q,
            part: 'snippet',
            type: 'video',
            maxResults: 48
        };
        if( channelId ) {
            params['channelId'] = channelId;
        }
        const request = gapi.client.youtube.search.list(params);
        return request;
    },
    showYouTubeModal: function() {
        $('#youTubeSearchModal').on('shown.bs.modal', function(){}).modal('show')
    },
    viewSource: function() {
        if( $('.nicehide').size() > 0 ) {
            $('.nicehide').addClass('nonicehide').removeClass('nicehide')
        }
    },
    submitClicked: function() {
        if( $('.nonicehide').size() > 0 ) {
            $('.nonicehide').addClass('nicehide').removeClass('nonicehide')
        }
        model.savePost();
        return false;
    },
    savePost: function() {
        $('#btnSubmit').html('<i class="fa fa-refresh fa-spin"></i> Saving...').attr('disabled', 'disabled')
        var form = model.objectifyForm( $('form[name="postForm"]').serializeArray() );
        $.ajax({
            url: '/blog/edit',
            dataType: 'json',
            data: form,
            method: 'POST',
            success: function(result) {
                $('#SYNCHRONIZER_TOKEN').val(result.token);
                $('#id').val(result.post.id);
                $('#version').val(result.post.version);
                window.history.pushState("", "", '/blog/edit/' + result.post.id);
            },
            error: function(e) {
                console.error(e);
                alert('Error saving post.  See console.')
            },
            complete: function(){
                $('#btnSubmit').html('Save').removeAttr('disabled')
            }
        })
    },
    previewPost: function(){
        $.ajax(
            {
                method: 'POST',
                dataType: 'html',
                url: '/blog/previewPost?c=' + new Date().getTime(),
                data: {
                    post: $('#wysihtml-textarea').val()
                },
                success: function(result) {
                    $('#previewBody').html('')
                    postscribe('#previewBody', result, {
                        done: function() {
                            // a bit hackish, but need to do what's in "onReady" of the post manually...
                            $('#previewBody').find('.spoiler').hide()
                            $('#previewBody').on('click', '.showSpoiler', function(){
                                $(this).closest('div').find('.spoiler').show()
                                $(this).hide()
                                return false
                            })
                        }
                    })
                    $('#previewModal').modal('show')
                },
                error: function(e) {
                    console.error(e);
                    alert('Error generating preview!')
                }
            }
        )
    },
    showHelpModal: function(){
        $('#helpModal').modal('show')
    },
    showGistModal: function(){
        $('#createGistModal').on('shown.bs.modal', function(){
            $('#createGistCode').val('')
            $('#createGistDescription').val('')
            $('#createGistName').val('')

            // create code editor
            if( !model.aceEditor ) {
                model.aceEditor = ace.edit("createGistCode");
                model.aceEditor.setTheme("ace/theme/dracula");
                model.aceEditor.session.setMode("ace/mode/javascript");
            }
            model.aceEditor.setValue('')
        })
        $('#createGistModal').modal('show')
    },
    createGist: function(){
        $('#createGistBtn').attr('disabled', 'disabled').html('<i class="fa fa-refresh fa-spin"></i> Creating...')
        $.ajax({
            url: '/blog/createGist',
            method: 'POST',
            data: {
                name: $('#createGistName').val(),
                description: $('#createGistDescription').val(),
                code: model.aceEditor.getValue()
            },
            success: function(result) {
                model.editor.focus();
                model.editor.composer.commands.exec("insertHTML","[gist2 id=" + result.gist.id + "]");
                $('#createGistModal').modal('hide')
            },
            error: function(e) {
                console.error(e);
                alert('Error posting Gist.  Check console.');
            },
            complete: function(){
                $('#createGistBtn').html('Create').removeAttr('disabled')
            }
        })
    },
    showS3Browser: function(){
        $('#s3Modal').on('shown.bs.modal', function(){
            $('#s3BrowserIframe').get(0).src += ' ';
        });
        $('#s3Modal').modal('show')
    },
    goFullscreen: function() {
        $('#editor').toggleClass('fullscreen')
        return false;
    },
    initIframe: function() {
        var f = document.querySelector('.wysihtml-sandbox');
        var iframeDoc = f.contentDocument || f.contentWindow.document;

        $('iframe').load(function(){
            var styles = 'br{content: ".";display: inline-block;width: 100%;border-bottom: 2px dashed red;}p{border:1px dotted}code{padding:2px 4px;font-size:90%;color:#c7254e;background-color:#f9f2f4;border-radius:4px}.alert{padding:15px;margin-bottom:20px;border:1px solid transparent;border-radius:4px}.alert-success{color:#3c763d;background-color:#dff0d8;border-color:#d6e9c6}.alert-warning{color:#8a6d3b;background-color:#fcf8e3;border-color:#faebcc}.alert-danger{color:#a94442;background-color:#f2dede;border-color:#ebccd1}.alert-info{color:#31708f;background-color:#d9edf7;border-color:#bce8f1}';
            $(iframeDoc).contents().find("head")
                .append($("<style type='text/css'>"+styles+"</style>"));
        });
    },
    initEditor: function() {
        model.editor = new wysihtml.Editor(
            "wysihtml-textarea", {
                insertsLineBreaksOnReturn: false,
                toolbar: "toolbar",
                parserRules: wysihtmlParserRules
            }
        )
        model.editor.on( "load", function() {
            // Trick browser into showing HTML5 required validation popups.
            $('#wysihtml-textarea').addClass('nicehide');
        } );
    },
    addTag: function() {
        $('#newTagModal').modal({show: true})
        $('#newTag').val('')
    },
    saveNewTag: function() {
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
    },
    keepSessionAlive: function() {
        setInterval(function(){
            // keep the session alive so that it doesn't expire in the middle of a blog post
            $.ajax({url: '/'})
        }, 30000)
    },
    initDatePickers: function() {
        $('.datepicker').datetimepicker({ format: dateFormat})
    },
    aceExtMapping: {
        'js': 'ace/mode/javascript',
        'ts': 'ace/mode/javascript',
        'groovy': 'ace/mode/groovy',
        'gsp': 'ace/mode/groovy',
        'gson': 'ace/mode/groovy',
        'java': 'ace/mode/java',
    },
    setAceMode: function() {
        var nameArr = $(this).val().split('.');
        var ext = nameArr[nameArr.length-1];
        var aceMode = model.aceExtMapping[ext];
        if( aceMode ) {
            model.aceEditor.session.setMode( aceMode )
        }
    },
    showS3UploadModal: function(){
        $('#s3UploadModal').on('shown.bs.modal', function(){
            $('.upload-row').not(':last').remove()
            $('.upload-folder, .upload-key, .upload-file').val('')
            model.s3Uploads = [
                new Upload(),
            ]
        })
        $('#s3UploadModal').modal('show')
    },
    canRemoveUpload: false,
    removeS3Upload: function(event, context) {
        const upload = context.upload;
        const idx = model.s3Uploads.indexOf(upload);
        if( idx != -1 ) {
            model.s3Uploads.splice( idx, 1 );
        }
        model.canRemoveUpload = model.s3Uploads.length != 1;
        return false;
    },
    s3Upload: function() {
        const formData = new FormData();
        model.s3Uploads.forEach( (upload, index)=> {
           formData.append(`folder_${index}`, upload.folder);
           formData.append(`key_${index}`, upload.key);
           formData.append(`upload_${index}`, $(`#uploadFile_${index}`).get(0).files[0]);
        });
        $('#uploadFileBtn').attr('disabled', 'disabled').html('<i class="fa fa-refresh fa-spin"></i> Uploading...')
        $.ajax({
            url: '/blog/uploadFile',
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(result) {
                $('#uploadFileBtn').removeAttr('disabled').html('Upload')
                result.urls.forEach( url => {
                    model.editor.focus();
                    model.editor.composer.commands.exec("insertHTML",`<img src="${url}" class="img-thumbnail img-responsive"/>`);
                })
                $('#s3UploadModal').modal('hide')
            },
            error: function(e) {
                console.error(e);
                $('#uploadFileBtn').removeAttr('disabled').html('Upload')
                alert('Error uploading file.  See console.');
            }
        })
    },
    addUpload: function() {
        model.s3Uploads.push( new Upload() );
        model.canRemoveUpload = model.s3Uploads.length != 1;
        return false;
    },
    objectifyForm: function(formArray) {
        var returnArray = {};
        for (var i = 0; i < formArray.length; i++){
            returnArray[formArray[i]['name']] = formArray[i]['value'];
        }
        return returnArray;
    },
    exitFullScreen: function(e) {
        if( e.which === 27 ) {
            $(this).removeClass('fullscreen')
        }
        //return false;
    },
    listTags: function(){
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
    },
};

$(document).ready(function(){
    model.initDatePickers();
    model.listTags();
    model.initEditor();
    model.keepSessionAlive();
    model.initIframe();
    rivets.bind($('body'), {model: model})
})
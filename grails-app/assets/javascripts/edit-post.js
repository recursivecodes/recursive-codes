$(document).ready(function(){
    $('.datepicker').datetimepicker({ format: dateFormat})
    //wysihtml.insertsLineBreaksOnReturn = true;
    editor = new wysihtml.Editor(
            "wysihtml-textarea", {
                insertsLineBreaksOnReturn: false,
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

    setInterval(function(){
        // keep the session alive so that it doesn't expire in the middle of a blog post
        $.ajax({url: '/'})
    }, 30000)

    $('.preview-post-trigger').on('click', function(){
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
    })

    $('.help-modal-trigger').on('click', function(){
        $('#helpModal').modal('show')
    })

    var f = document.querySelector('.wysihtml-sandbox');
    var iframeDoc = f.contentDocument || f.contentWindow.document;

    $('iframe').load(function(){
        const styles = `
            p {border: 1px dotted;}
            code {
                padding: 2px 4px;
                font-size: 90%;
                color: #c7254e;
                background-color: #f9f2f4;
                border-radius: 4px;
            }
            .alert {
                padding: 15px;
                margin-bottom: 20px;
                border: 1px solid transparent;
                border-radius: 4px;
            }
            .alert-success { 
                color: #3c763d;
                background-color: #dff0d8;
                border-color: #d6e9c6;
            }
            .alert-warning { 
                color: #8a6d3b;
                background-color: #fcf8e3;
                border-color: #faebcc;
            }
            .alert-danger { 
                color: #a94442;
                background-color: #f2dede;
                border-color: #ebccd1;
            }
            .alert-info { 
                color: #31708f;
                background-color: #d9edf7;
                border-color: #bce8f1;
            }
        `;
        $(iframeDoc).contents().find("head")
            .append($("<style type='text/css'>"+styles+"</style>"));
    })


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
<%--
  Created by IntelliJ IDEA.
  User: 558848
  Date: 3/7/2016
  Time: 11:08 AM
--%>

<%@ page import="codes.recursive.blog.Tag; codes.recursive.blog.Post" contentType="text/html;charset=UTF-8" %>
<%
    codes.recursive.blog.Post post = post
    List<codes.recursive.blog.Tag> tags = post.postTags.sort{it.tag.name}
%>
<html>
<head>
    <meta name="layout" content="main"/>
    <meta name="description" content="${post?.summary ? post?.summary : ui.makeDescription(article: post.article)}" />
    <meta name="keywords" content="${post?.postTags?.collect{it.tag.name}.join(',')}${post?.keywords ? ',' + post.keywords : ''}" />
    <meta name="author" content="${post?.authoredBy?.firstName} ${post?.authoredBy?.lastName}" />
    <meta property="og:title" content="${post.title}" />
    <meta property="og:type" content="website" />
    <meta property="og:description" content="${post?.summary ? post?.summary : ui.makeDescription(article: post.article)}">
    <meta property="og:image" content="${raw(post.bannerImg)}" />
    <meta property="og:url" content="https://recursive.codes${request.forwardURI}" />
    <meta property="og:site_name" content="recursive.codes">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:site" content="@recursivecodes">
    <title>${post.title}</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/js/all.min.js"></script>
    <asset:stylesheet src="social-share.min.css"/>
    <asset:javascript src="social-share.min.js"/>
    <style>
        .img-wrapper::after {
            font-size: 12px;
            content: 'Click Image To View Full Size';
            display: block;
        }
        #imgViewer::-webkit-scrollbar {
            -webkit-appearance: none;
            height: 10px;
        }
        #imgViewer::-webkit-scrollbar-thumb {
            border-radius: 5px;
            background-color: rgba(0,0,0,.5);
            box-shadow: 0 0 1px rgba(255,255,255,.5);
        }
    </style>
    <g:javascript>
        $().ready(function(){
            $('img').not('.img-related-thumb').each(function(i,e) {
                $(e).wrap('<div class="img-wrapper"></div>')
            })

            $('img').not('.img-related-thumb').on('click', function(e) {
              $('#imgViewer').html('').append( $(e.currentTarget).clone().removeClass('img-responsive').removeClass('img-thumbnail') )
              $('#viewImg').modal('show')
            })

            $('iframe').each(function(i,e) {
              var el = $(e);
              if( el.attr('src') && el.attr('src').indexOf("youtube") >= 0 ) {
                el.wrap('<div class="embed-responsive embed-responsive-16by9"></div>')
              }
            })

        })
    </g:javascript>
</head>

<body>
    <h1 id="postTitle">${post.title}</h1>
    <h5>Posted By: ${post.authoredBy.firstName} ${post.authoredBy.lastName} on <g:formatDate date="${post.publishedDate}"/></h5>
    <g:if test="${tags.size()}">
        <h6 class="upper">Tagged:
        <g:each in="${tags}" var="pt" status="i">
            <g:link action="tagged" controller="page" params="${[tag: pt.tag.name]}">${pt.tag.name}</g:link><g:if test="${i != post.postTags.size()-1}">, </g:if>
        </g:each>
        </h6>
    </g:if>
    <div class="post-container" id="post-container">
        <ui:render bannerImg="${raw(post.bannerImg)}" post="${raw(post.article)}" />
    </div>

    <%--
    <hr/>
    <div class=" pad-bottom-10">
        <ui:adsense />
    </div>
    --%>
    <hr/>
    <div id="share" class="ss-box ss-shadow ss-responsive text-center mb-3"></div>

    <hr/>

    <h3>Related Posts</h3>

    <div class="row">
        <g:each in="${relatedPosts}" var="relatedPost">
            <div class="col-lg-4 col-md-6 col-sm-12">
                <g:link controller="blog" action="post" id="${relatedPost.id}">
                    <img src="${relatedPost.bannerImg}" alt="${relatedPost.title}" class="img-related-thumb img-thumbnail" />
                    <h3>${relatedPost.title}</h3>
                </g:link>
                <p>
                    <ui:truncatePost article="${relatedPost.article}"/>
                </p>
            </div>
        </g:each>
    </div>

    <div class="alert alert-info">
        <b>Note:</b> Comments are currently closed on this blog. Disqus is simply too bloated to justify its use with the low volume of comments on this blog. Please visit my <a href="/page/contact">contact page</a> if you have something to say!
    </div>

    <%--
    <ui:disqus id="${post.id}" />
    --%>

    <ui:modal id="viewImg" closable="true" large="true" title="View Image">
        <div id="imgViewer" style="overflow-x: scroll;">
        </div>
    </ui:modal>
</body>
</html>
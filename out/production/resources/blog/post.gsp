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
    <title>${post.title} - recursive.codes</title>
    <asset:stylesheet src="jssocials.css"/>
    <asset:stylesheet src="jssocials-theme-flat.css"/>
    <asset:javascript src="jssocials.js"/>
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
            $('img').each(function(i,e) {
                $(e).wrap('<div class="img-wrapper"></div>')
            })

            jsSocials.shares.reddit = {
                label: "Reddit",
                logo: "fa fa-reddit-alien",
                shareUrl: "https://reddit.com/submit?url={url}&title=" + $.trim(document.querySelector('#postTitle').textContent),
                countUrl: ""
            };
            $("#share").jsSocials({
                shares: ["reddit","twitter", "facebook", "googleplus", "linkedin", "pinterest", "whatsapp", "email"]
            });

            $('img').on('click', function(e) {
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
    <div class="">
        <ui:render post="${raw(post.article)}" />
    </div>

    <%--
    <hr/>
    <div class=" pad-bottom-10">
        <ui:adsense />
    </div>
    --%>
    <hr/>
    <div id="share"></div>
    <hr/>

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
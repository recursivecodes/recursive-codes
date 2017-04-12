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
    <g:javascript>
        $().ready(function(){
            $("#share").jsSocials({
                shares: ["twitter", "facebook", "googleplus", "linkedin", "pinterest", "whatsapp", "email"]
            });
        })
    </g:javascript>
</head>

<body>
    <h1>${post.title}</h1>
    <h5>Posted By: ${post.authoredBy.firstName} ${post.authoredBy.lastName} on <g:formatDate date="${post.publishedDate}"/></h5>
    <g:if test="${tags.size()}">
        <h6 class="upper">Tagged:
        <g:each in="${tags}" var="pt" status="i">
            <g:link action="tagged" controller="page" params="${params << [tag: pt.tag.name]}">${pt.tag.name}</g:link><g:if test="${i != post.postTags.size()-1}">, </g:if>
        </g:each>
        </h6>
    </g:if>
    <div class="">
        <ui:render post="${raw(post.article)}" />
    </div>
    <hr/>

    <div class=" pad-bottom-10">
        <ui:adsense />
    </div>

    <hr/>
    <div id="share"></div>
    <hr/>

    <ui:disqus id="${post.id}" />

</body>
</html>
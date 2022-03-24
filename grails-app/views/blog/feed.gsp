<?xml version="1.0" encoding="utf-8"?>
<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:content="http://purl.org/rss/1.0/modules/content/">
    <channel>
        <title>recursive.codes</title>
        <link>${grailsApplication.config.grails.serverURL}</link>
        <description>The personal blog of Todd Sharp</description>
        <atom:link href="${grailsApplication.config.grails.serverURL}/blog/feed" rel="self" type="application/rss+xml" />
        <image>
            <url>${grailsApplication.config.grails.serverURL}${assetPath(src: 'logo.png')}</url>
            <title>recursive.codes</title>
            <link>${grailsApplication.config.grails.serverURL}</link>
        </image>
        <g:each in="${posts}" var="post">
            <item>
                <title>${HtmlUtils.htmlEscape(post.title)}</title>
                <link>${grailsApplication.config.grails.serverURL}/blog/post/${post.id}</link>
                <guid>${grailsApplication.config.grails.serverURL}/blog/post/${post.id}</guid>
                <pubDate>${formatter.format(post.dateCreated)}</pubDate>
                <description>${post.summary}</description>
                <content:encoded><![CDATA[ ${post.article} ]]></content:encoded>
                <enclosure url="${post.bannerImg}" type="image/png" length="${post.bannerImg.toURL().getBytes().size()}" />
            </item>
        </g:each>
    </channel>
</rss>
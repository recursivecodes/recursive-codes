<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
    <channel>
        <title>recursive.codes</title>
        <link>${grailsApplication.config.grails.serverURL}</link>
        <description>The personal blog of Todd Sharp</description>
        <atom:link href="${grailsApplication.config.grails.serverURL}/blog/feed" rel="self" type="application/rss+xml" />
        <image>
            <url>${grailsApplication.config.grails.serverURL}${assetPath(src: 'favicon.ico')}</url>
            <title>recursive.codes</title>
            <link>${grailsApplication.config.grails.serverURL}</link>
        </image>
        <g:each in="${posts}" var="post">
            <item>
                <title>${post.title}</title>
                <link>${grailsApplication.config.grails.serverURL}/blog/post/${post.id}</link>
                <pubDate>${formatter.format(post.dateCreated)}</pubDate>
                <description><![CDATA[ ${post.article} ]]></description>
                <enclosure url="${post.bannerImg}" />
            </item>
        </g:each>
    </channel>
</rss>
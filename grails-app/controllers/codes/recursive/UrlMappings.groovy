package codes.recursive

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/blog/feed/all"{
            controller = 'blog'
            action = 'feed'
            all = 'true'
        }
        "/videos"(controller: 'page', action: 'videos')
        "/p/$slug"(controller: 'blog', action: 'bySlug')
        "/presentations"(controller: 'page', action: 'videos')
        "/subscribe"(controller: 'page', action: 'subscribe')
        "/unsubscribe"(controller: 'page', action: 'unsubscribe')
        "/search"(controller: 'page', action: 'search')
        "/sitemap**"(controller: 'page', action: 'sitemap')
        "/import"(controller: 'page', action: 'importPosts')
        "/"(controller: 'page', action: 'index')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}

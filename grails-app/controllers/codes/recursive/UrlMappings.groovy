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
        "/presentations"(controller: 'page', action: 'videos')
        "/search"(controller: 'page', action: 'search')
        "/sitemap**"(controller: 'page', action: 'sitemap')
        "/"(controller: 'page', action: 'index')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}

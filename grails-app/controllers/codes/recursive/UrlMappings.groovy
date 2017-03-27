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
        "/"(controller: 'page', action: 'index')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}

package codes.recursive

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/"(controller: 'page', action: 'index')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}

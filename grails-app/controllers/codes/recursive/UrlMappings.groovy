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
        "/privacy"(controller: 'page', action: 'privacy')
        "/videos"(controller: 'page', action: 'videos')
        "/p/$slug"(controller: 'blog', action: 'bySlug')
        "/presentations"(controller: 'page', action: 'presentations')
        "/subscribe"(controller: 'page', action: 'subscribe')
        "/unsubscribe"(controller: 'page', action: 'unsubscribe')
        "/search"(controller: 'page', action: 'search')
        "/sitemap**"(controller: 'page', action: 'sitemap')
        "/import"(controller: 'page', action: 'importPosts')
        "/updateBannerImages"(controller: 'page', action: 'updateBannerImgs')
        "/updateNullBannerImages"(controller: 'page', action: 'updateNullBannerImages')
        "/"(controller: 'page', action: 'index')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}

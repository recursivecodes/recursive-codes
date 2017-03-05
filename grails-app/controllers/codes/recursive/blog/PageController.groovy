package codes.recursive

import codes.recursive.blog.BlogService
import grails.plugin.springsecurity.annotation.Secured

@Secured('permitAll')
class PageController extends AbstractController{

    BlogService blogService

    def index() {
        def model = defaultModel
        def max = params.long('max') ?: 10
        def offset = params.long('offset') ?: 0
        return model << [
                posts: blogService.listPublished(max, offset),
                postCount: blogService.countPublished(),
                offset: offset,
                max: max,
        ]
    }

    def tagged() {
        def model = defaultModel
        def max = params.long('max') ?: 10
        def offset = params.long('offset') ?: 0
        def tag = params?.tag
        if( !tag ) {
            redirect(action: 'index')
            return
        }

        model << [
                posts: blogService.listTagged(tag, max, offset),
                postCount: blogService.countTagged(tag),
                offset: offset,
                max: max,
                tag: tag,
        ]

        render(view: 'index', model: model)
    }

    def about() {
        def model = defaultModel
        return model << [:]
    }

    def contact() {
        def model = defaultModel
        return model << [:]
    }
}
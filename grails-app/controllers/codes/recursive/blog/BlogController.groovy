package codes.recursive.blog

import codes.recursive.PageController
import codes.recursive.admin.AbstractAdminController
import codes.recursive.admin.command.PostCommand
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import java.text.DateFormat
import java.text.SimpleDateFormat

class BlogController extends AbstractAdminController {

    BlogService blogService

    @Secured('permitAll')
    def post() {
        def model = new PageController().defaultModel
        def postId = params.long('id')
        def post = blogService.findById(postId)

        if ( !postId || !post || !post.canBeViewedBy(currentUser) ) {
            response.status = 404
            redirect(controller: 'page', action: 'index')
            return
        }
        return model << [
                post: post,
        ]
    }

    @Secured('permitAll')
    def feed() {
        def model = defaultModel
        DateFormat pubDateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        response.setContentType("application/xml")
        return model << [
                posts    : blogService.listPublished(25, 0),
                formatter: pubDateFormatter,
        ]
    }

    @Secured('ROLE_ADMIN')
    def list() {
        def model = defaultModel
        def max = params.long('max') ?: 25
        def offset = params.long('offset') ?: 0
        return model << [posts: blogService.list(max, offset), postCount: blogService.count()]
    }

    @Secured('ROLE_ADMIN')
    def edit(PostCommand command) {
        def model = defaultModel
        def post = params.long('id') ? blogService.findById(params.long('id')) : new Post()
        def defaultParams = [id: params.long('id')]

        if (request.method == 'POST') {
            // Enforce XSRF token

            withForm {
                // shame on you grails...when you have a multiple select and only choose one value grails binds it as a string, not a list.
                command.tags = params.list('tags')

                // Validate input
                command.validate()

                // Enforce optimistic locking on existing instances
                if (post.id && command.version < post.version) {
                    flash.error = g.message(code: 'default.optimistic.locking.failure')
                    redirect(action: 'edit', params: defaultParams)
                    return
                }
                // If we're valid, populate our model and save
                if (!command.hasErrors()) {
                    command.populatePost(post)

                    blogService.save(post)

                    // Tell the end user about it and redirect back to the form
                    flash.message = g.message(code: 'admin.blog.post.saved')

                    // Redirect to prevent the old "reload saves it again" issue,
                    redirect(action: 'list')
                    return

                } else {
                    // Command has errors, send the user a message
                    flash.error = g.message(code: 'default.error.message')
                }
            }
        }
        // On an initial GET, we need to populate our command
        if (request.method == 'GET') {
            command = command.fromPost(post)
        }
        return model << [
                command      : command,
                defaultParams: defaultParams,
        ]
    }

    @Secured('ROLE_ADMIN')
    def publish() {
        def model = defaultModel
        def redirectTo = params.redirectTo ?: 'list';
        def postId = params.long('postId')
        if (!postId) {
            redirect(action: 'list')
            return
        }
        def publish = params.boolean('publish')
        def post = blogService.findById(postId)
        post.isPublished = publish
        blogService.save(post)
        flash.message = g.message(code: post.isPublished ? 'admin.blog.post.published' : 'admin.blog.post.deleted')
        redirect(action: redirectTo)
        return
    }

    @Secured('ROLE_ADMIN')
    def ajaxListTags() {
        render(blogService.listTags() as JSON)
    }

    @Secured('ROLE_ADMIN')
    def ajaxSaveTag() {
        def name = params.tag
        def tag = new Tag(name: name)
        try {
            blogService.saveTag(tag)
        }
        catch (e) {
            // just swallow the error - it's likely a 'unique' violation -- let them assume they created the new one after the list gets rebuilt (to avoid user confusion)
        }
        render([saved: true] as JSON)
    }
}
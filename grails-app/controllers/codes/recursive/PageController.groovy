package codes.recursive

import codes.recursive.blog.BlogService
import codes.recursive.blog.command.ContactFormCommand
import grails.core.GrailsApplication
import grails.plugin.springsecurity.annotation.Secured
import grails.plugins.mail.MailService

@Secured('permitAll')
class PageController extends AbstractController{

    BlogService blogService
    GrailsApplication grailsApplication
    MailService mailService

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
        // cheap way to do it, but, why not...
        redirect( action: 'post', controller: 'blog', id: 2)
        return
        return model << [:]
    }

    def contact(ContactFormCommand command) {
        def model = defaultModel
        def defaultParams = [id: params.long('id')]

        if (request.method == 'POST') {
            // Enforce XSRF token

            withForm {
                command.validate()

                if (!command.hasErrors()) {

                    def msg = mailService.sendMail {
                        subject "recursive.codes contact from ${command.name}"
                        text "Message from: ${command.name} (${command.email}).\n\nMessage: ${command.comments}"
                        to grailsApplication.config.codes.recursive.email
                        from grailsApplication.config.codes.recursive.email
                    }



                    // Tell the end user about it and redirect back to the form
                    flash.message = g.message(code: 'contact.form.success')

                    // Redirect to prevent the old "reload saves it again" issue,
                    redirect(action: 'contact')
                    return

                } else {
                    // Command has errors, send the user a message
                    flash.error = g.message(code: 'default.error.message')
                }
            }
        }
        // On an initial GET, we need to populate our command
        if (request.method == 'GET') {
            command = new ContactFormCommand()
        }
        return model << [
                command      : command,
                defaultParams: defaultParams,
                returnUrl: grailsApplication.config.grails.serverURL,
        ]
    }
}
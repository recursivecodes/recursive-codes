package codes.recursive.subscriber

import codes.recursive.admin.AbstractAdminController
import codes.recursive.blog.Subscriber
import grails.plugin.springsecurity.annotation.Secured

class SubscriberController extends AbstractAdminController {

    SubscriberService subscriberService

    @Secured('ROLE_ADMIN')
    def list() {
        def model = defaultModel
        def max = params.long('max') ?: 25
        def offset = params.long('offset') ?: 0
        return model << [subscribers: subscriberService.list(max, offset), subscriberCount: subscriberService.count()]
    }

    @Secured('permitAll')
    def deactivate(Subscriber subscriber) {
        subscriberService.deactivate(subscriber)
        flash.message = g.message(code: 'subscription.deactivated')
        redirect(action: 'list')
        return
    }

    @Secured('permitAll')
    def activate(Subscriber subscriber) {
        subscriberService.activate(subscriber)
        flash.message = g.message(code: 'subscription.activated')
        def ref = request.getHeader('referer')
        redirect(action: 'list')
        return
    }

    @Secured('permitAll')
    def verify() {
        def token = params.get("token")
        subscriberService.verify(token)
        flash.message = g.message(code: 'subscription.email.verified')
        redirect(controller: 'page', action: 'index')
        return
    }

    @Secured('ROLE_ADMIN')
    def edit(Subscriber subscriber) {
        def model = defaultModel
        def defaultParams = [id: params.long('id')]
        if( !subscriber ) {
            subscriber = params.get('id') ? subscriberService.findById(params.get('id')) : new Subscriber()
        }

        if (request.method == 'POST') {
            withForm {
                subscriber.validate()
                if (subscriber.id && subscriber.version < subscriber.version) {
                    flash.error = g.message(code: 'default.optimistic.locking.failure')
                    redirect(action: 'edit', params: defaultParams)
                    return
                }
                // If we're valid, populate our model and save
                if (!subscriber.hasErrors()) {
                    subscriberService.save(subscriber)
                    subscriberService.sendVerification(subscriber)
                    redirect(controller: 'subscriber', action: 'list')
                    return

                } else {
                    // Command has errors, send the user a message
                    flash.error = g.message(code: 'default.error.message')
                    redirect(controller: 'subscriber', action: 'edit')
                    return
                }
            }
        }
        return model << [
                subscriber      : subscriber,
                defaultParams: defaultParams,
        ]

    }



}

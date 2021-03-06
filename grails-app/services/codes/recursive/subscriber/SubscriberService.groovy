package codes.recursive.subscriber

import codes.recursive.blog.Post
import codes.recursive.blog.Subscriber
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import grails.plugins.mail.MailService

@Transactional
class SubscriberService {

    MailService mailService
    GrailsApplication grailsApplication

    def sendVerification(Subscriber subscriber) {
        def body = """<p>Thanks for subscribing to my blog!</p>
<p>Please click on the following link to verify your subscription:</p>
<p><a href="${grailsApplication.config.grails.serverURL}/subscriber/verify?token=${subscriber.verificationToken}">${grailsApplication.config.grails.serverURL}/subscriber/verify?token=${subscriber.verificationToken}</a></p>
"""
        mailService.sendMail {
            subject "[recursive.codes] - Verify Your Subscription"
            html body
            to subscriber.email
            from grailsApplication.config.codes.recursive.email
        }
    }

    def notifySubscribers(Post post) {
        listActive().each { Subscriber sub ->
            def body = """<p>A new post is available on https://recursive.codes!</p>
<p>${post.title}</p> 
<p><a href="${grailsApplication.config.grails.serverURL}/p/${post.slug}">${grailsApplication.config.grails.serverURL}/p/${post.slug}</a></p>
<p><small><a href="${grailsApplication.config.grails.serverURL}/unsubscribe?id=${sub.id}">Unsubscribe</a> at any time.</small></p>
"""

            mailService.sendMail {
                subject "[recursive.codes] - New Post: ${post.title}"
                html body
                to sub.email
                from grailsApplication.config.codes.recursive.email
            }
        }
    }

    def listActive() {
        return Subscriber.findByIsActiveAndIsVerified(true, true)
    }

    def list() {
        return Subscriber.list()
    }

    def count() {
        return Subscriber.count()
    }

    def list(Long max, Long offset) {
        return Subscriber.list([sort: 'dateCreated', order: 'desc', max: max, offset: offset])
    }

    def save(Subscriber subscriber) {
        return subscriber.save(flush: true)
    }

    def activate(Subscriber subscriber) {
        subscriber.isActive = true
        return subscriber.save(flush: true)
    }

    def deactivate(Subscriber subscriber) {
        subscriber.isActive = false
        return subscriber.save(flush: true)
    }

    def deactivate(String id) {
        return deactivate(findById(id))
    }

    def delete(Subscriber subscriber) {
        return subscriber.delete(flush: true)
    }

    def delete(String id) {
        return findById(id).delete(flush: true)
    }

    def deleteByEmail(String email) {
        return findByEmail(email).delete(flush: true)
    }

    def verify(String token) {
        Subscriber sub = Subscriber.findByVerificationToken(token)
        sub.isActive = true
        sub.isVerified = true
        sub.verificationToken = null
        return save(sub)
    }

    def findById(String id) {
        return Subscriber.findById(id)
    }

    def findByEmail(String email) {
        return Subscriber.findByEmail(email)
    }
}

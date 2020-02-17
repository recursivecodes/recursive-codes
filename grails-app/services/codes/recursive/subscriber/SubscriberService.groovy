package codes.recursive.subscriber

import codes.recursive.blog.Post
import codes.recursive.blog.Subscriber
import grails.gorm.transactions.Transactional
import grails.plugins.mail.MailService

@Transactional
class SubscriberService {

    MailService mailService

    def sendVerification(Subscriber subscriber) {
        def body = """Thanks for subscribing to my blog!
Please click on the following link to verify your subscription:
<a>${grailsApplication.config.grails.serverURL}/subscriber/verify?token=${subscriber.verificationToken}</a>
"""
        mailService.sendMail {
            subject "[recursive.codes] - Verify Your Subscription"
            html body
            to subscriber.email
            from grailsApplication.config.codes.recursive.email
        }
    }

    def notifySubscribers(Post post) {
        def body = """<p>A new post is available on https://recursive.codes!</p>
<p>${post.title}</p> 
<p><a href="${grailsApplication.config.grails.serverURL}/p/${post.slug}">${grailsApplication.config.grails.serverURL}/p/${post.slug}</a></p>
<p><small><a href="${grailsApplication.config.grails.serverURL}/unsubscribe">Unsubscribe</a> at any time.</small></p>
"""
        mailService.sendMail {
            subject "[recursive.codes] - New Post: ${post.title}"
            html body
            to grailsApplication.config.codes.recursive.email
            bcc list().collect { it.email }
            from grailsApplication.config.codes.recursive.email
        }
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

    def deactivate(String email) {
        return deactivate(findByEmail(email))
    }

    def deactivate(Long id) {
        return deactivate(findById(id))
    }

    def delete(Subscriber subscriber) {
        return subscriber.delete(flush: true)
    }

    def delete(Long id) {
        return findById(id).delete(flush: true)
    }

    def delete(String email) {
        return findByEmail(email).delete(flush: true)
    }

    def verify(String token) {
        Subscriber sub = Subscriber.findByVerificationToken(token)
        sub.isActive = true
        sub.isVerified = true
        sub.verificationToken = null
        return save(sub)
    }

    def findById(Long id) {
        return Subscriber.findById(id)
    }

    def findByEmail(String email) {
        return Subscriber.findByEmail(email)
    }
}

package codes.recursive


import codes.recursive.blog.BlogService
import codes.recursive.blog.Subscriber
import codes.recursive.blog.command.ContactFormCommand
import codes.recursive.subscriber.SubscriberService
import grails.converters.JSON
import grails.core.GrailsApplication
import grails.plugin.awssdk.s3.AmazonS3Service
import grails.plugin.springsecurity.annotation.Secured
import grails.plugins.mail.MailService
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.cache.CacheManager

import javax.annotation.PostConstruct

@Secured('permitAll')
@Slf4j
class PageController extends AbstractController{

    BlogService blogService
    SubscriberService subscriberService
    GrailsApplication grailsApplication
    MailService mailService
    AmazonS3Service amazonS3Service
    CacheManager grailsCacheManager
    Util util

    @PostConstruct
    void init() {
        this.amazonS3Service.client.clientOptions.pathStyleAccess = true
        this.amazonS3Service.client.clientOptions.chunkedEncodingDisabled = true
        this.amazonS3Service.client.endpoint = "${grailsApplication.config.codes.recursive.aws.s3.namespace}.compat.objectstorage.${grailsApplication.config.codes.recursive.aws.s3.region}.oraclecloud.com"
    }

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

    def privacy() {
        def model = defaultModel
        return model << [:]
    }

    def unsubscribe() {
        String id = params.get('id')
        subscriberService.deactivate(id)
        flash.message = g.message(code: 'subscription.deactivated')
        redirect(action: 'index')
        return
    }

    def subscribe() {
        def model = defaultModel
        def sub = new Subscriber()

        if( request.method == "POST" ) {
            def email = params.get('email')
            sub = new Subscriber(email: email)
            if( sub.validate() ) {
                subscriberService.save(sub)
                subscriberService.sendVerification(sub)
                flash.message = g.message(code: 'subscription.email.sent')
                redirect(action: 'subscribe')
                return
            }
            else {
                flash.error = g.message(code: 'default.error.message')
            }

        }

        return model << [
                subscriber: sub,
                defaultParams: [:],
        ]
    }

    def contact(ContactFormCommand command) {
        def model = defaultModel
        def defaultParams = [id: params.long('id')]

        if (request.method == 'POST') {
            // Enforce XSRF token

            withForm {
                if(util.isSpam(command.comments)) {
                    // <sarcasm> all good dude, no problems here! </sarcasm>
                    log.info("Contact form with spam from [${command?.name} : ${command.email}] was rejected. Comment: [${command.comments}]")
                    flash.message = g.message(code: 'contact.form.success')
                    redirect(action: 'contact')
                    return
                }

                command.validate()

                if (!command.hasErrors()) {
                    def msg = mailService.sendMail {
                        subject "recursive.codes contact from ${command.name}"
                        text "Message from: ${command.name} (${command.email}).\n\nMessage: ${command.comments}"
                        to grailsApplication.config.codes.recursive.email
                        from grailsApplication.config.codes.recursive.email
                    }

                    flash.message = g.message(code: 'contact.form.success')
                    redirect(action: 'contact')
                    return

                } else {
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

    def videos() {
        def model = defaultModel

        def entries = grailsCacheManager.getCache('object').get('youtubeEntries')?.get()
        def cachedAt = grailsCacheManager.getCache('object').get('youtubeEntriesCachedAt')?.get()
        def cacheExpired = cachedAt ? ( groovy.time.TimeCategory.minus( new Date(), cachedAt )?.hours > 4 ) : false
        if( !entries || cacheExpired ) {
            def feedUrl = grailsApplication.config.codes.recursive.youtubeFeed
            def feed = feedUrl.toURL().text
            entries = new XmlSlurper().parseText(feed).declareNamespace(media: 'http://search.yahoo.com/mrss/', yt: 'http://www.youtube.com/xml/schemas/2015')
            grailsCacheManager.getCache('object').put('youtubeEntries', entries)
            grailsCacheManager.getCache('object').put('youtubeEntriesCachedAt', new Date())
        }

        return model << [
                entries: entries,
                channelUrl: grailsApplication.config.codes.recursive.youtubeChannel,
                dateFormat: "yyyy-MM-dd'T'HH:mm:ssXXX",
        ]
    }

    def presentations() {
        def model = defaultModel
        def configBucket = grailsApplication.config.codes.recursive.aws.configBucket
        def tempFilePath = '/tmp/presentations.json'
        def tempFile = amazonS3Service.getFile('presentations.json', tempFilePath)
        def presentations = new JsonSlurper().parse(tempFile)
        return model << [
                presentations: presentations,
        ]
    }

    def search() {
        def model = defaultModel
        def results = []
        def searchString = params.get('searchString')
        def max = params.int('max') ?: 25
        def offset = params.int('offset') ?: 0

        if( searchString ) {
            results = blogService.searchPosts(searchString, max, offset)
        }
        return model << [
                results: results,
                searchString: searchString,
                baseUrl: grailsApplication.config.grails.serverURL,
        ]
    }

    def sitemap() {
        def sitemapName = request.forwardURI.tokenize('/').last()
        def tempFile = File.createTempFile(sitemapName.tokenize('.').first(), ".xml")
        def obj = amazonS3Service.getFile("sitemaps/${sitemapName}", tempFile.absolutePath)
        if( !obj ) {
            throw new Exception("Could not find a sitemap matching ${sitemapName}!")
        }
        def xml = tempFile.text
        tempFile.delete()
        render(text: xml, contentType: "text/xml", encoding: "UTF-8")
        /*
        response.setHeader "Content-disposition", "attachment; filename=${sitemap}"
        response.contentType = 'text/xml'
        response.outputStream << obj.getBytes()
        response.outputStream.flush()
        tempFile.delete()
        */
    }

    def importPosts() {
        ImportBlogPostJob.triggerNow();
        render([complete: true] as JSON)
        return
    }
}
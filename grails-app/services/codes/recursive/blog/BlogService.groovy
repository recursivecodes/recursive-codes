package codes.recursive.blog

import codes.recursive.AbstractService
import codes.recursive.subscriber.SubscriberService
import groovy.sql.Sql

/**
 * Created by Todd Sharp on 2/25/2017.
 */
class BlogService extends AbstractService {
    Sql sql
    SubscriberService subscriberService

    def findById(Long id) {
        return Post.findById(id)
    }

    def findBySlug(String slug) {
        return Post.findBySlug(slug)
    }

    def findByImportId(String importId) {
        return Post.findByImportedId(importId)
    }

    def save(Post post) {
        def notifySubs = !post.id
        post.save(flush: true, failOnError: true)
        if( notifySubs ) {
            subscriberService.notifySubscribers(post)
        }
        return post
    }

    def listTagged(String tag, Long max, Long offset) {
        return Post.executeQuery('select p from Post p join p.postTags pt where pt.tag.name = ? and p.publishedDate <= ? and p.isPublished = ?', [tag, new Date(), true], [sort: 'publishedDate', order: 'desc', max: max, offset: offset])
    }

    def countTagged(String tag) {
        def tagged = Post.executeQuery('select count(p) as tags from Post p join p.postTags pt where pt.tag.name = ? and p.publishedDate <= ? and p.isPublished = ?', [tag, new Date(), true])
        return tagged?.first()
    }

    def listWithoutBanners() {
        return Post.findAllByBannerImgIsNull()
    }

    def list() {
        return Post.list([sort: 'publishedDate', order: 'desc'])
    }

    def list(Long max, Long offset) {
        return Post.list([sort: 'publishedDate', order: 'desc', max: max, offset: offset])
    }

    def count() {
        return Post.count()
    }

    def listPublished(Long max, Long offset) {
        return Post.findAllByIsPublishedAndPublishedDateLessThanEquals(true, new Date(), [sort: 'publishedDate', order: 'desc', max: max, offset: offset])
    }

    def listPublished() {
        return Post.findAllByIsPublishedAndPublishedDateLessThanEquals(true, new Date(), [sort: 'publishedDate', order: 'desc'])
    }

    def countPublished() {
        return Post.countByIsPublishedAndPublishedDateLessThanEquals(true, new Date())
    }

    def findTagByTagName(String tagName) {
        return Tag.findByName(tagName)
    }

    def listTags() {
        return Tag.list([sort: 'name'])
    }

    def saveTag(Tag tag) {
        return tag.save(flush: true)
    }

    def searchPosts(String q, int max, int offset) {
        def posts = Post.createCriteria().list {
            eq('isPublished', true)
            lte('publishedDate', new Date())
            or{
                ilike('title', "%${q}%")
                ilike('article', "%${q}%")
            }
            maxResults(max)
            firstResult(offset)
        }
        def count = Post.createCriteria().count {
            eq('isPublished', true)
            lte('publishedDate', new Date())
            or{
                ilike('title', "%${q}%")
                ilike('article', "%${q}%")
            }
        }
        return [
                posts: posts,
                count: count,
        ]
    }

}

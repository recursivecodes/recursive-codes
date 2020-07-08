package codes.recursive.blog

import codes.recursive.AbstractService
import codes.recursive.subscriber.SubscriberService
import groovy.sql.Sql
import org.hibernate.SessionFactory

/**
 * Created by Todd Sharp on 2/25/2017.
 */
class BlogService extends AbstractService {
    Sql sql
    SubscriberService subscriberService
    SessionFactory sessionFactory

    def findById(Long id) {
        return Post.findById(id)
    }

    def findBySlug(String slug) {
        return Post.findBySlug(slug)
    }

    def findByImportId(String importId) {
        return Post.findByImportedId(importId)
    }

    def findRelatedPosts(Post post) {
        def session = sessionFactory.currentSession
        def qry = """
select id, title, article, authored_by_id, is_published, version, published_date, date_created, last_updated, keywords, summary, imported_id, slug, imported_on, banner_img
from (
     select 1 as q, id, title, DBMS_LOB.substr(article, 4000) as article, authored_by_id, is_published, version, published_date, date_created, last_updated, keywords,
            summary, imported_id, slug, imported_on, banner_img
     from post p
     where id in (
         select ip.id
         from post ip
                  left join post_tag ipt on ip.id = ipt.post_id
         where ipt.tag_id in (select iipt.tag_id from post_tag iipt where iipt.post_id = :postId)
         and ip.is_published = 1
         and ip.published_date < sysdate
     )
     and id != :postId
     and p.is_published = 1
     and p.published_date < sysdate

     union

     select 2 as q, id, title, DBMS_LOB.substr(article, 4000) as article, authored_by_id, is_published, version, published_date, date_created, last_updated, keywords,
            summary, imported_id, slug, imported_on, banner_img
     from post p2
     where p2.is_published = 1
     and p2.published_date < sysdate
)
order by q, published_date desc
FETCH FIRST 3 ROWS ONLY
"""
        def sqlQuery = session.createSQLQuery(qry)

        List<Post> relatedPosts = sqlQuery.with {
            addEntity(Post)
            setLong('postId', post.id)
            list()
        }
        return relatedPosts
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

package codes.recursive.admin.command

import codes.recursive.User
import codes.recursive.blog.Post
import codes.recursive.blog.PostTag
import codes.recursive.blog.Tag
import grails.databinding.BindingFormat
import grails.validation.Validateable

/**
 * Created by Todd Sharp on 2/25/2017.
 */
class PostCommand implements Validateable{

    // properties
    String title
    String article
    String keywords
    String summary
    @BindingFormat('MM/dd/yyyy hh:mm a')
    Date publishedDate = new Date()
    Boolean isPublished = true
    User authoredBy
    Integer version
    List tags = []

    static PostCommand fromPost(Post post) {
        def command = new PostCommand(
                title: post?.title,
                keywords: post?.keywords,
                summary: post?.summary,
                article: post?.article,
                publishedDate: post?.publishedDate ?: new Date(),
                isPublished: post?.isPublished,
                authoredBy: post?.authoredBy,
                version: post?.version ?: 0,
                tags: post.postTags ? post.postTags.collect { it.tag } : [],
        )
        return command
    }

    void populatePost(Post post) {
        post.title = this?.title
        post.keywords = this?.keywords
        post.summary = this?.summary
        post.article = this?.article
        post.publishedDate = this?.publishedDate
        post.isPublished = this?.isPublished
        post.authoredBy = this?.authoredBy

        post.postTags.collect().each { pt ->
            post.removeFromPostTags(pt)
            pt.delete()
        }

        this.tags.each {
            post.addToPostTags( new PostTag(post: post, tag: Tag.findById(it)))
        }
    }

    static constraints = {
        title nullable: false, maxSize: 500
        keywords nullable: true, maxSize: 500
        summary nullable: true, maxSize: 500
        article nullable: false
        authoredBy nullable: false
        publishedDate nullable: false
        tags minSize: 1
    }
}



package codes.recursive

import codes.recursive.blog.BlogService
import codes.recursive.blog.Post
import codes.recursive.blog.PostTag
import codes.recursive.blog.Tag
import codes.recursive.subscriber.SubscriberService
import codes.recursive.user.UserService
import com.amazonaws.services.s3.model.ObjectMetadata
import grails.core.GrailsApplication
import grails.plugin.awssdk.s3.AmazonS3Service
import groovy.json.JsonSlurper
import org.apache.commons.io.FilenameUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import java.text.SimpleDateFormat

class UpdateBannerImageJob {

    BlogService blogService

    static triggers = {
        cron name: 'updateBannerImagesTrigger', cronExpression: "0 0 0 1 * ? *"
    }

    def execute() {
        println 'Update banner image job running...'
        int updatedPosts = 0

        List<Post> posts = blogService.list()
        posts.each { Post post ->
            def parsedArticle = Jsoup.parseBodyFragment(post.article)
            Element bannerImgTag = parsedArticle.select("[src*=/banner_]")?.first()
            if( bannerImgTag ) {
                bannerImgTag.remove()
                post.bannerImg = bannerImgTag.attr('src')
                post.article = parsedArticle.select("body").html()
                blogService.save(post)
                updatedPosts++
                println "Updated banner image for ${post.title}..."
            }
        }

        println "Update banner image job complete! ${updatedPosts} posts have been updated."
    }
}

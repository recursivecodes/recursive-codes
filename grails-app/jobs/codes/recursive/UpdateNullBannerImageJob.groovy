package codes.recursive

import codes.recursive.blog.BlogService
import codes.recursive.blog.Post
import com.amazonaws.services.s3.model.ObjectMetadata
import grails.core.GrailsApplication
import grails.plugin.awssdk.s3.AmazonS3Service
import groovy.json.JsonSlurper
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag

import java.awt.Graphics2D
import java.awt.image.BufferedImage

class UpdateNullBannerImageJob {

    BlogService blogService
    GrailsApplication grailsApplication
    AmazonS3Service amazonS3Service

    static triggers = {
        cron name: 'updateNullBannerImagesTrigger', cronExpression: "0 0 0 1 * ? *"
    }

    def execute() {
        println 'Update null banner image job running...'

        // set compatibility for OCI Object Storage
        amazonS3Service.client.clientOptions.pathStyleAccess = true
        amazonS3Service.client.clientOptions.chunkedEncodingDisabled = true
        amazonS3Service.client.endpoint = "${grailsApplication.config.codes.recursive.aws.s3.namespace}.compat.objectstorage.${grailsApplication.config.codes.recursive.aws.s3.region}.oraclecloud.com"
        String uploadBucket = grailsApplication.config.codes.recursive.aws.s3.imgBucket

        JsonSlurper slurper = new JsonSlurper()
        int updatedPosts = 0

        def transferAsset = { URL url, String path ->
            def s3Config = grailsApplication.config.codes.recursive.aws.s3
            boolean exists = amazonS3Service.exists(uploadBucket, path)
            if( !exists ) {
                ObjectMetadata md = new ObjectMetadata()
                md.addUserMetadata("X-Imported-From", url.toString())

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection()
                urlConnection.addRequestProperty("User-Agent", "RecursiveCodes/1.0")
                InputStream urlStream = urlConnection.getInputStream()

                amazonS3Service.storeInputStream(
                        uploadBucket,
                        path,
                        urlStream,
                        md
                )
            }
            return "${s3Config.endpoint}/n/${s3Config.namespace}/b/${s3Config.imgBucket}/o/${path}".toString()
        }

        List<Post> posts = blogService.listWithoutBanners()
        URL pixabayImagesUrl = "https://pixabay.com/api/?key=${grailsApplication.config.codes.recursive.pixabay.apiKey}&q=nature&orientation=horizontal&per_page=200".toURL()
        def imgJson = slurper.parse(pixabayImagesUrl)

        posts.each { Post post ->
            Collections.shuffle(imgJson.hits)
            def randomImg = imgJson.hits.first()
            URL imageUrl = randomImg.largeImageURL.toURL()
            String bannerFileName = FilenameUtils.getName(imageUrl.path)
            String path = post.id + '/banner_' + bannerFileName
            String newUrl = transferAsset(imageUrl, path)
            post.article += """<p>Image by <a href="https://pixabay.com/users/${randomImg.user}-${randomImg.user_id}">${randomImg.user}</a> from <a href="https://pixabay.com">Pixabay</a></p>"""
            post.bannerImg = newUrl
            blogService.save(post)
            updatedPosts++
        }

        println "Update null banner image job complete! ${updatedPosts} posts have been updated."
    }
}

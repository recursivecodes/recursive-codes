package codes.recursive

import codes.recursive.blog.BlogService
import codes.recursive.blog.Post
import codes.recursive.blog.Sitemap
import codes.recursive.blog.SitemapService
import codes.recursive.blog.Tag
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.redfin.sitemapgenerator.ChangeFreq
import com.redfin.sitemapgenerator.WebSitemapGenerator
import com.redfin.sitemapgenerator.WebSitemapUrl
import grails.core.GrailsApplication
import grails.plugin.awssdk.s3.AmazonS3Service
import grails.util.Environment

import javax.annotation.PostConstruct

class CreateSitemapJob {

    GrailsApplication grailsApplication
    BlogService blogService
    SitemapService sitemapService
    AmazonS3Service amazonS3Service

    static triggers = {
      simple repeatInterval: 1000 * 60 * 60 // execute job once an hour
    }

    def execute() {

        if( Environment.current == Environment.DEVELOPMENT ) {
            println 'Exiting sitemap job because Environment == DEVELOP'
            return false
        }

        amazonS3Service.client.clientOptions.pathStyleAccess = true
        amazonS3Service.client.clientOptions.chunkedEncodingDisabled = true
        amazonS3Service.client.endpoint = "${grailsApplication.config.codes.recursive.aws.s3.namespace}.compat.objectstorage.${grailsApplication.config.codes.recursive.aws.s3.region}.oraclecloud.com"

        sitemapService.list().each { it ->
            try {
                amazonS3Service.deleteFile("sitemaps/${it.fileName}")
            }
            catch(e) {
                println "Could not delete ${it.fileName} from S3 bucket...."
                // fail silently
            }
        }
        sitemapService.deleteAll()

        def tmpPath = System.getProperty("java.io.tmpdir")
        def tempDir = new File( tmpPath )
        def baseUrl = grailsApplication.config.grails.serverURL
        WebSitemapGenerator wsg = WebSitemapGenerator.builder(baseUrl, tempDir).build()
        wsg.addUrl(new WebSitemapUrl.Options("${baseUrl}/about").lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.MONTHLY).build())
        wsg.addUrl(new WebSitemapUrl.Options("${baseUrl}/search").lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.YEARLY).build())
        wsg.addUrl(new WebSitemapUrl.Options("${baseUrl}/videos").lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.DAILY).build())
        wsg.addUrl(new WebSitemapUrl.Options("${baseUrl}/presentations").lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.MONTHLY).build())
        wsg.addUrl(new WebSitemapUrl.Options("${baseUrl}/contact").lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.YEARLY).build())

        blogService.listPublished().each { Post post ->
            wsg.addUrl(new WebSitemapUrl.Options("${baseUrl}/blog/post/${post.id}").lastMod(post.lastUpdated).priority(0.8).changeFreq(ChangeFreq.WEEKLY).build())
        }

        blogService.listTags().each { Tag tag ->
            wsg.addUrl(new WebSitemapUrl.Options("${baseUrl}/page/tagged/?tag=${tag.name}").lastMod(new Date()).priority(0.8).changeFreq(ChangeFreq.WEEKLY).build())
        }

        def files = wsg.write()
        def idx = wsg.writeSitemapsWithIndex()

        files.each { File file ->
            def key = file.name
            sitemapService.save( new Sitemap(fileName: key ) )
            amazonS3Service.storeFile("sitemaps/${key}", file, CannedAccessControlList.Private )
            file.delete()
        }

        sitemapService.save( new Sitemap(fileName: 'sitemap_index.xml', isIndex: true ) )
        def idxFile = new File(tmpPath + '/sitemap_index.xml')
        amazonS3Service.storeFile("sitemaps/sitemap_index.xml", idxFile, CannedAccessControlList.Private )
        idxFile.delete()

        println 'Sitemaps created!'
    }
}

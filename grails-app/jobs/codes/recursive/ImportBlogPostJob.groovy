package codes.recursive

import codes.recursive.blog.BlogService
import codes.recursive.blog.Post
import codes.recursive.blog.PostTag
import codes.recursive.blog.Tag
import codes.recursive.user.UserService
import com.amazonaws.services.s3.model.ObjectMetadata
import grails.core.GrailsApplication
import grails.plugin.awssdk.s3.AmazonS3Service
import groovy.json.JsonSlurper
import org.apache.commons.io.FilenameUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import java.text.SimpleDateFormat

class ImportBlogPostJob {

    GrailsApplication grailsApplication
    BlogService blogService
    UserService userService
    AmazonS3Service amazonS3Service

    static triggers = {
        cron name: 'importPostsTrigger', cronExpression: "0 0 19 1/1 * ? *"
    }

    def execute() {
        println 'Import job running...'

        // set compatibility for OCI Object Storage
        amazonS3Service.client.clientOptions.pathStyleAccess = true
        amazonS3Service.client.clientOptions.chunkedEncodingDisabled = true
        amazonS3Service.client.endpoint = "${grailsApplication.config.codes.recursive.aws.s3.namespace}.compat.objectstorage.${grailsApplication.config.codes.recursive.aws.s3.region}.oraclecloud.com"

        // set up some vars
        int importedPosts = 0
        String apiUsername = grailsApplication.config.get('codes.recursive.import.api.username')
        String apiKey = grailsApplication.config.get('codes.recursive.import.api.key')
        String uploadBucket = grailsApplication.config.codes.recursive.aws.s3.imgBucket
        int currentListPostsPage = 1
        List existingTags = blogService.listTags()
        User me = userService.findByUsername("admin")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSX")
        String baseUrl = grailsApplication.config.grails.serverURL

        def listPostsUrl = { page ->
            return "https://app.compendium.com/api/posts?Page=${page ?: 1}&Deleted=false&business_unit_id=[%22d4446287-6cf2-4969-b0cd-a95e8d13817c%22]&Sort=DisplayTimestamp&Order=Descending&UserId[]=f1047a3a-1d1c-46ba-9cdb-6fd5928d0a1c&UserId[]=6d16180a-2af9-4a93-9ea8-15b22f226be4&timezone=America/New_York".toString().toURL()
        }

        def authenticatedApiCall = { url ->
            def connection = (HttpURLConnection)url.openConnection()
            connection.setRequestMethod("GET")
            connection.setRequestProperty("Authorization", "Basic " + "${apiUsername}:${apiKey}".bytes.encodeBase64().toString())
            connection.setRequestProperty("Accept", "application/vnd.compendium.blog;version=2,application/json")
            return new JsonSlurper().parse( connection.getInputStream() )
        }

        Map allPostsJson = authenticatedApiCall( listPostsUrl(currentListPostsPage) )
        int allPostsCount = allPostsJson.stats.total
        List importedPostsList = allPostsJson.content

        // get all pages of posts
        (2..Math.ceil(allPostsCount / 20)).each { it ->
            currentListPostsPage = it
            allPostsJson = authenticatedApiCall( listPostsUrl(currentListPostsPage) )
            importedPostsList.addAll( allPostsJson.content )
        }

        // all tags in this DB to avoid multiple lookups add new tags to this array as they are created below
        Map importedCategories = authenticatedApiCall( "https://app.compendium.com/api/blogs/category?network_id=e7c690e8-6ff9-102a-ac6d-e4aebca50425&business_unit_id=%5B%22d4446287-6cf2-4969-b0cd-a95e8d13817c%22%5D".toURL() )

        def findCategoryById = { id ->
            return importedCategories.results.find{ it.id == id }.title ?: "Unknown"
        }

        def transferAsset = { URL url, String path ->
            def s3Config = grailsApplication.config.codes.recursive.aws.s3
            boolean exists = amazonS3Service.exists(uploadBucket, path)
            if( !exists ) {
                ObjectMetadata md = new ObjectMetadata()
                md.addUserMetadata("X-Imported-From", url.toString())
                InputStream urlStream = url.openStream()
                amazonS3Service.storeInputStream(
                        uploadBucket,
                        path,
                        urlStream,
                        md
                )
            }
            return "${s3Config.endpoint}/n/${s3Config.namespace}/b/${s3Config.imgBucket}/o/${path}".toString()
        }

        // loop over list of post, getting details and importing if needed
        importedPostsList.eachWithIndex { Map importedPost, int i ->
            def importId = importedPost.id
            def existingPost = blogService.findByImportId(importId)

            if( !existingPost || ( !existingPost.isPublished && importedPost.is_published && importedPost.is_live ) ) {
                // get post details
                def postDetails = authenticatedApiCall("https://app.compendium.com${importedPost.url}".toURL())
                def postTags = []
                postDetails.category_ids.each {
                    postTags << findCategoryById(it)
                }

                List keywords = []
                String description = ""
                postDetails.custom_properties.each {
                    String fieldId = it.FieldId.toLowerCase()
                    if( fieldId.contains('description') ) {
                        description = it.Value
                    }
                    if( fieldId.contains('wordpress_tag_field') ) {
                        keywords.addAll( it.taxonomy_items.collect { k, v -> return v } )
                    }
                }
                Date pubDate = postDetails.publish_date ? dateFormat.parse(postDetails.publish_date) : null

                if( !pubDate ) {
                    use (groovy.time.TimeCategory) {
                        pubDate = new Date() + 1.year
                    }
                }

                // create Post
                Post newPost = existingPost ?: new Post()
                newPost.title = importedPost.title.toString().decodeHTML()
                newPost.keywords = keywords.join(', ')
                newPost.summary = description
                newPost.publishedDate = pubDate
                newPost.isPublished = postDetails.is_published
                newPost.authoredBy = me
                newPost.importedId = importId
                newPost.slug = importedPost.url_lookup_token
                newPost.importedOn = new Date()
                newPost.postTags = []

                // create Tags (if not exist)
                postTags.each { tagName ->
                    def tag = existingTags.find{ it.name == tagName }
                    if( !tag ) {
                        tag = new Tag(name: tagName)
                        blogService.saveTag(tag)
                        existingTags << tag
                    }
                    newPost.postTags << new PostTag(post: newPost, tag: tag)
                }

                // parse body
                def parsedBody = Jsoup.parseBodyFragment(postDetails.body.toString().decodeHTML())

                // copy images to my object storage
                parsedBody.select("img").each { Element img ->
                    String src = img.attributes.src
                    URL srcUrl = src.toURL()
                    if( src.contains("cdn.app.compendium.com/uploads/") ) {
                        String fileName = FilenameUtils.getName(srcUrl.path)
                        String path = importId + '/' + fileName
                        String newUrl = transferAsset(srcUrl, path)
                        img.attr("src", newUrl)
                    }
                }

                // update links to other posts on my blog
                parsedBody.select("a").each { Element link ->
                    String href = link.attributes.href
                    try {
                        URL hrefUrl = href.toURL()
                        if( href.contains("blogs.oracle.com/developers/") ) {
                            String slug = hrefUrl.getPath()
                            link.attr("href", "${baseUrl}/p${slug.replace("/developers", "")}")
                        }
                    }
                    catch (MalformedURLException e) {
                        // ignore it, it's an anchor link
                    }
                }

                // move & update featured image url & update body
                String banner = postDetails.featured_image
                if( banner ) {
                    URL bannerUrl = banner.toURL()
                    Element bannerEl = new Element(org.jsoup.parser.Tag.valueOf("img"), "")
                    bannerEl.addClass("img-thumbnail").addClass("img-responsive")
                    String bannerFileName = FilenameUtils.getName(bannerUrl.path)
                    String path = importId + '/banner_' + bannerFileName
                    String newBannerUrl = transferAsset(bannerUrl, path)
                    bannerEl.attr("src", newBannerUrl)
                    parsedBody.select("body").first().children().first().before(bannerEl)
                }

                // fix "alert" classes
                parsedBody.select('.success,.info,.warning,.danger').each { Element e ->
                    def existingClass = e.attr('class')
                    e.attr('class', 'alert alert-' + existingClass)
                }

                // remove style blocks
                parsedBody.select("style").remove()

                // update gists to shortcode
                parsedBody.select("script").each { Element script ->
                    String src = script.attributes.src
                    URL srcUrl = src.toURL()
                    String gistId = FilenameUtils.getBaseName(srcUrl.getPath())
                    String shortCode = "[gist2 id=${gistId}]"
                    Element gistWrapper = new Element(org.jsoup.parser.Tag.valueOf("span"), "")
                    gistWrapper.text(shortCode)
                    script.replaceWith( gistWrapper )
                }

                // save post
                newPost.article = parsedBody.select("body").html()
                blogService.save(newPost)
                println "Imported post with ID: ${importId} as ${newPost.title}!"
                importedPosts++
            }
            else {
                // post exists, leave it alone...
                // not going to support "updating" existing
                // imported posts for now...
                println "Skipping ${importedPost.title} because it's already been imported..."
            }
        }

        println "Import job complete! ${importedPosts} have been imported."
    }
}

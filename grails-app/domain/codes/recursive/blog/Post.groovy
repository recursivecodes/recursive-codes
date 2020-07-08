package codes.recursive.blog

import codes.recursive.Role
import codes.recursive.User
import grails.util.Holders


class Post {

    String title
    String keywords
    String summary
    String article
    String bannerImg
    Date publishedDate
    Boolean isPublished = true
    Long version
    User authoredBy
    Date dateCreated
    Date lastUpdated
    String importedId
    String slug
    Date importedOn

    def isPublicallyAvailable() {
        return isPublished && publishedDate.before(new Date())
    }

    def canBeViewedBy(User user) {
        if( user?.authorities?.find { it.authority == Role.ROLE_ADMIN } ) return true
        if (!isPublicallyAvailable()) return false
        return true
    }

    static hasMany = [postTags: PostTag]

    static mapping = {
        id generator: 'sequence', params: [ sequence: Holders.config.codes.recursive.oracle.sequence.post ]
    }

    static constraints = {
        title nullable: false, maxSize: 500
        slug nullable: true, maxSize: 1000
        importedId nullable: true, maxSize: 36
        keywords nullable: true, maxSize: 500
        summary nullable: true, maxSize: 500
        article nullable: false
        bannerImg nullable: true, maxSize: 2000
        authoredBy nullable: false
        publishedDate nullable: false
        importedOn nullable: true
    }

}

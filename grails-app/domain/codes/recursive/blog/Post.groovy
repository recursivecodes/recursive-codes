package codes.recursive.blog

import codes.recursive.Role
import codes.recursive.User


class Post {

    String title
    String keywords
    String summary
    String article
    Date publishedDate
    Boolean isPublished = true
    User authoredBy
    Date dateCreated
    Date lastUpdated

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
        id generator: "native"
    }

    static constraints = {
        title nullable: false, maxSize: 500
        keywords nullable: true, maxSize: 500
        summary nullable: true, maxSize: 500
        article nullable: false
        authoredBy nullable: false
        publishedDate nullable: false
    }

}

package codes.recursive.blog

import codes.recursive.User


class Post {

    String title
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
        if (!isPublicallyAvailable()) return false
        return true
    }

    static hasMany = [postTags: PostTag]

    static mapping = {
        id generator: "native"
    }

    static constraints = {
        title nullable: false, maxSize: 500
        article nullable: false
        authoredBy nullable: false
        publishedDate nullable: false
    }

}
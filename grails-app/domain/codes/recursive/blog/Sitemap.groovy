package codes.recursive.blog

class Sitemap {

    String fileName
    Boolean isIndex=false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        fileName unique: true
    }
}

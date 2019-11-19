package codes.recursive.blog

class Sitemap {

    String fileName
    Boolean isIndex=false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        id generator:'sequence', params: [ sequence: 'ISEQ$$_33774' ]
        fileName unique: true
    }
}

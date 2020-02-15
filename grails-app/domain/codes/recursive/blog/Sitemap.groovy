package codes.recursive.blog

import grails.util.Holders

class Sitemap {

    String fileName
    Boolean isIndex=false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        id generator:'sequence', params: [ sequence: Holders.config.codes.recursive.oracle.sequence.sitemap ]
        fileName unique: true
    }
}

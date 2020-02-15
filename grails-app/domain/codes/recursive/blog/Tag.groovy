package codes.recursive.blog

import grails.util.Holders

class Tag {

    String name
    Date dateCreated
    Date lastUpdated

    static mapping = {
        id generator:'sequence', params: [ sequence: Holders.config.codes.recursive.oracle.sequence.tag ]
    }

    static constraints = {
        name nullable: false, maxSize: 100, unique: true
    }
}

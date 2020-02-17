package codes.recursive.blog

import codes.recursive.Role
import codes.recursive.User
import grails.util.Holders


class Subscriber {

    String email
    String verificationToken = UUID.randomUUID().toString()
    Boolean isActive = false
    Boolean isVerified = false
    Long version
    Date dateCreated
    Date lastUpdated

    static mapping = {
        id generator: 'sequence', params: [ sequence: Holders.config.codes.recursive.oracle.sequence.post ]
    }

    static constraints = {
        email nullable: false, unique: true, email: true, maxSize: 500
        verificationToken nullable: true, maxSize: 36
        isActive nullable: false
        isVerified nullable: false
    }

}

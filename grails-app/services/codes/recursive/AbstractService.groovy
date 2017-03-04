package codes.recursive

import codes.recursive.User
import grails.plugin.springsecurity.SpringSecurityService
import groovy.sql.Sql

abstract class AbstractService {

    Sql sql
    SpringSecurityService springSecurityService

    User getCurrentUser() {
        return springSecurityService.currentUser
    }

}

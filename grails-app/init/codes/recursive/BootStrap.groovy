package codes.recursive

import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
        if( Environment.current == Environment.DEVELOPMENT ) {
            if( !User.findByUsername('admin') ){
                def tempPassword = UUID.randomUUID().toString().replace('-', '').substring(0,10)
                def user = new User(firstName: 'Admin', lastName: 'User', username: 'admin', password: tempPassword)
                user.addToUserRoles(role: Role.findByAuthority(Role.ROLE_ADMIN))
                user.save(flush: true, failOnError: true)
                println "User 'admin' created with temporary password ${tempPassword}. Please log in and change."
            }
        }
        CreateSitemapJob.triggerNow()
    }
    def destroy = {
    }
}

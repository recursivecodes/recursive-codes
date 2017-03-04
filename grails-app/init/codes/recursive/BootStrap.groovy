package codes.recursive

import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
        if( Environment.current == Environment.DEVELOPMENT ) {
            if( !User.findByUsername('admin') ){
                def user = new User(firstName: 'Todd', lastName: 'Sharp', username: 'admin', password: 'password')
                user.addToUserRoles(role: Role.findByAuthority(Role.ROLE_ADMIN))
                user.save(flush: true, failOnError: true)
            }
        }
    }
    def destroy = {
    }
}

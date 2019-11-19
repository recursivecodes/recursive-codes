package codes.recursive.admin.command

import codes.recursive.Role
import codes.recursive.User
import codes.recursive.UserRole
import codes.recursive.blog.Post
import codes.recursive.blog.PostTag
import codes.recursive.blog.Tag
import grails.databinding.BindingFormat
import grails.validation.Validateable

/**
 * Created by Todd Sharp on 2/25/2017.
 */
class EditUserCommand implements Validateable{

    // properties
    Long id
    String firstName
    String lastName
    String username
    String password
    boolean enabled = true
    boolean accountExpired=false
    boolean accountLocked=false
    boolean passwordExpired=false
    Long version=1

    static EditUserCommand fromUser(User user) {
        def command = new EditUserCommand(
                id: user?.id,
                firstName: user?.firstName,
                lastName: user?.lastName,
                username: user?.username,
                password: user?.password,
                enabled: user?.enabled,
                accountExpired: user?.accountExpired,
                accountLocked: user?.accountLocked,
                passwordExpired: user?.passwordExpired,
                version: user?.version ?: 1,
        )
        return command
    }

    void populateUser(User user) {
        user.firstName = this?.firstName
        user.lastName = this?.lastName
        user.username = this?.username
        user.password = this?.password
        user.enabled = this?.enabled
        user.accountExpired = this?.accountExpired
        user.accountLocked = this?.accountLocked
        user.passwordExpired = this?.passwordExpired
        user.version = this?.version

        if( !user?.userRoles?.size() ){
            user.addToUserRoles(new UserRole(role: Role.findByAuthority(Role.ROLE_ADMIN)))
        }
    }

    static constraints = {
        id nullable: true
        username validator: { val, obj ->
            def existingUser = User.findByUsername(val)
            if( existingUser && existingUser.id != obj.id ) {
                return 'admin.user.invalid.username'
            }
        }
        password blank: false, password: true
        firstName nullable: false
        lastName nullable: false
    }
}



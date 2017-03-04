package codes.recursive.user

import codes.recursive.User
import codes.recursive.admin.AbstractAdminController
import codes.recursive.admin.command.EditUserCommand
import grails.plugin.springsecurity.annotation.Secured

@Secured('ROLE_ADMIN')
class UserController extends AbstractAdminController{

    UserService userService

    def index(){
        redirect(action: 'list')
    }

    def list(){
        def model = defaultModel
        def max = params.long('max') ?: 25
        def offset = params.long('offset') ?: 0
        return model << [
                users: userService.list(max, offset),
                totalUsers: userService.count(),
        ]
    }

    def edit(EditUserCommand command) {
        def model = defaultModel
        def userId = params.long('id')
        def user = userService.findById(userId) ?: new User()

        def defaultParams = [id: userId]

        if (request.method == 'POST') {
            // Enforce XSRF token

            withForm {
                // Validate input
                command.validate()

                // Enforce optimistic locking on existing instances
                if (user.id && command.version < user.version) {
                    flash.error = g.message(code: 'default.optimistic.locking.failure')
                    redirect(action: 'edit', params: defaultParams)
                    return
                }
                // If we're valid, populate our model and save
                if (!command.hasErrors()) {
                    command.populateUser(user)

                    userService.save(user)

                    // Tell the end user about it and redirect back to the form
                    flash.message = g.message(code: 'admin.user.saved')

                    // Redirect to prevent the old "reload saves it again" issue,
                    redirect(action: 'list')
                    return

                } else {
                    // Command has errors, send the user a message
                    flash.error = g.message(code: 'default.error.message')
                }
            }
        }
        // On an initial GET, we need to populate our command
        if (request.method == 'GET') {
            command = command.fromUser(user)
        }
        return model << [
                command      : command,
                defaultParams: defaultParams,
        ]
    }
}
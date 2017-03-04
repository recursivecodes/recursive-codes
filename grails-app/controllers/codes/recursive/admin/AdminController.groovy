package codes.recursive.admin

import grails.plugin.springsecurity.annotation.Secured

@Secured('ROLE_ADMIN')
class AdminController extends AbstractAdminController{

    def index(){
        def model = defaultModel
        return model << [
                :
        ]
    }
}
package codes.recursive.admin

import grails.plugin.springsecurity.annotation.Secured

@Secured('ROLE_ADMIN')
class AdminController extends AbstractAdminController{

    def index(){
        redirect(controller: 'blog', action: 'list')
    }
}
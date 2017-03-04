package codes.recursive.admin

import codes.recursive.AbstractController
import codes.recursive.User
import grails.plugin.springsecurity.SpringSecurityService

class AbstractAdminController extends AbstractController {
    SpringSecurityService springSecurityService

    Map getDefaultModel() {
        def model = super.defaultModel
        model.menu = [
                [
                        text: 'Blog',
                        action: 'index',
                        controller: 'page',
                ],
                [
                        text   : 'Users',
                        submenu: [
                                [
                                        text      : 'List Users',
                                        controller: 'user',
                                        action    : 'list',
                                ],
                                [
                                        text      : 'New User',
                                        controller: 'user',
                                        action    : 'edit',
                                ],
                        ]
                ],
                [
                        text   : 'Posts',
                        submenu: [
                                [
                                        text      : 'List Posts',
                                        controller: 'blog',
                                        action    : 'list',
                                ],
                                [
                                        text      : 'New Post',
                                        controller: 'blog',
                                        action    : 'edit',
                                ],
                        ]
                ]
        ]
        model.currentUser = currentUser
        return model
    }

    User getCurrentUser() {
        return springSecurityService.currentUser
    }
}
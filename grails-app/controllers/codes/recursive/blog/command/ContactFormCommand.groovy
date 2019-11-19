package codes.recursive.blog.command

import grails.validation.Validateable

/**
 * Created by Todd Sharp on 3/8/2017.
 */
class ContactFormCommand implements Validateable{
    String name
    String email
    String comments

    static constraints = {
        name maxsize: 100, nullable: false
        email email: true, nullable: false
        comments nullable: false
    }
}



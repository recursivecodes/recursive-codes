package codes.recursive

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class Util {

    Boolean isSpam(String msg) {

        def plino = 'https://plino.herokuapp.com/api/v1/classify/'
        def body = [email_text: msg]
        def result = [:]
        def spam = true //pessimistic mode

        def post = new URL(plino).openConnection()
        post.setRequestMethod("POST")
        post.setDoOutput(true)
        post.setRequestProperty(
                "Content-Type", "application/json"
        )
        post.getOutputStream().write(
                JsonOutput.toJson(body).getBytes("UTF-8")
        )
        if(post.getResponseCode() == 200) {
            result = new JsonSlurper().parseText( post.inputStream.text )
            spam = result?.email_class == "spam"
        }
        return spam
    }

}
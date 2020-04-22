package codes.recursive

import grails.util.Holders

abstract class AbstractController{
    Map getDefaultModel(){
        def config = Holders.getGrailsApplication().config
        return [
                commitHash: config?.codes?.recursive?.commitHash ?: 'unknown',
        ]
    }
}
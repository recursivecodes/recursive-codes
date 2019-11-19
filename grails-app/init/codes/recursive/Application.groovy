package codes.recursive

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        final GrailsApp app = new GrailsApp(Application)
        app.run(args)
    }
}
package codes.recursive.blog

import codes.recursive.AbstractService

class SitemapService extends AbstractService {

    static String INDEX_NAME = 'sitemap_index.xml'

    def findByFileName(String fileName) {
        return Sitemap.findByFileName(fileName)
    }

    def findById(Long id) {
        return Sitemap.findById(id)
    }

    def list() {
        return Sitemap.list()
    }

    def listNoIndex() {
        return Sitemap.findAllByIsIndex(false)
    }

    def list(Long max, Long offset) {
        return Sitemap.list([max: max, offset: offset])
    }

    def count() {
        return Sitemap.count()
    }

    def save(Sitemap sitemap) {
        return sitemap.save(flush: true)
    }

    def delete(Sitemap sitemap) {
        return sitemap.delete(flush: true)
    }

    def deleteAll() {
        return Sitemap.executeUpdate("delete from Sitemap")
    }

}
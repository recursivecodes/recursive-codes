package codes.recursive.user

import codes.recursive.AbstractService
import codes.recursive.User

class UserService extends AbstractService {

    def findById(Long id) {
        return User.findById(id)
    }

    def list(Long max, Long offset) {
        return User.list([max: max, offset: offset])
    }

    def count() {
        return User.count()
    }

    def save(User user) {
        return user.save(flush: true)
    }

    def delete(User user) {
        return user.delete(flush: true)
    }

}
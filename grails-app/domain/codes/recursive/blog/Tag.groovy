package codes.recursive.blog

class Tag {

    String name
    Date dateCreated
    Date lastUpdated

    static mapping = {
        id generator:'sequence', params: [ sequence: 'ISEQ$$_33777' ]
    }

    static constraints = {
        name nullable: false, maxSize: 100, unique: true
    }
}

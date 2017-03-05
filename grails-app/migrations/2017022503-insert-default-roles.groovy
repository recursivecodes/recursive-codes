databaseChangeLog = {

    changeSet(author: "toddsharp", id: "2017022503-insert-default-roles") {
        insert(tableName: 'role') {
            column(name: 'authority', value: 'ROLE_ADMIN')
            column(name: 'last_updated', valueComputed: 'curdate()')
        }

        insert(tableName: 'role') {
            column(name: 'authority', value: 'ROLE_AUTHOR')
            column(name: 'last_updated', valueComputed: 'curdate()')
        }
    }
}

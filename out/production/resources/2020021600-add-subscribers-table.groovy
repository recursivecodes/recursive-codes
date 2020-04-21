databaseChangeLog = {
    changeSet(author: "toddsharp", id: "2020021600-add-subscribers-table") {
        createTable(tableName: 'subscriber') {
            column(name: 'id', type: "INT", autoIncrement: true) {
                constraints(primaryKey: true)
            }
            column(name: 'email', type: 'NVARCHAR(500)') {
                constraints(nullable: false)
            }
            column(name: 'is_active', type: 'BIT', defaultValue: 0) {
                constraints(nullable: false)
            }
            column(name: 'version', type: 'INT', defaultValue: 0) {
                constraints(nullable: false)
            }
            column(name: 'date_created', type: 'DATETIME', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
            column(name: 'last_updated', type: 'DATETIME', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
        }
    }
}

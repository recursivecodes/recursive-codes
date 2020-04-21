databaseChangeLog = {
    changeSet(author: "toddsharp", id: "2020021800-drop-re-add-subscribers-table") {
        dropTable(tableName: 'subscriber')
        createTable(tableName: 'subscriber') {
            column(name: 'id', type: "NVARCHAR(36)") {
                constraints(primaryKey: true)
            }
            column(name: 'email', type: 'NVARCHAR(500)') {
                constraints(nullable: false)
            }
            column(name: 'is_active', type: 'BIT', defaultValue: 0) {
                constraints(nullable: false)
            }
            column(name: 'verification_token', type: 'NVARCHAR(36)') {
                constraints(nullable: true)
            }
            column(name: 'is_verified', type: 'BIT', defaultValue: 0) {
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

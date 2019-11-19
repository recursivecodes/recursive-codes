databaseChangeLog = {

    changeSet(author: "toddsharp", id: "2018080200-create-sitemap-table") {
        createTable(tableName: 'sitemap') {
            column(name: 'id', type: "INT", autoIncrement: true) {
                constraints(primaryKey: true)
            }
            column(name: 'file_name', type: 'NVARCHAR(100)') {
                constraints(nullable: false)
            }
            column(name: 'is_index', type: 'BIT', defaultValue: 0) {
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

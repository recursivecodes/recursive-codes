databaseChangeLog = {
    changeSet(author: "toddsharp", id: "2020021400-add-import-columns") {
        addColumn(tableName: 'post') {
            column(name: 'imported_id', type: 'NVARCHAR(36)') {
                constraints(nullable: true)
            }
        }
        addColumn(tableName: 'post') {
            column(name: 'slug', type: 'NVARCHAR(1000)') {
                constraints(nullable: true)
            }
        }
        addColumn(tableName: 'post') {
            column(name: 'imported_on', type: 'DATETIME') {
                constraints(nullable: true)
            }
        }
    }
}
